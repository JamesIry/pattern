/*
 * Copyright (c) 2007-2013 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cascading.pattern.pmml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.planner.PlannerException;
import cascading.pattern.PatternPlatformTestCase;
import cascading.pipe.Pipe;
import cascading.pipe.assembly.Discard;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class PMMLPlatformTestCase extends PatternPlatformTestCase
  {
  private static final Logger LOG = LoggerFactory.getLogger( PMMLPlatformTestCase.class );

  public static final String DATA_PATH = System.getProperty( "test.data.path", "../pattern-pmml/src/test/resources/pmml/" );
  private String resultPath;

  protected String getResultPath()
    {
    if( resultPath == null )
      resultPath = getOutputPath( "pmml/results/" + getTestName() );

    return resultPath;
    }

  @Override
  public void setUp() throws Exception
    {
    super.setUp();

    enableLogging( "cascading.pattern", "debug" );
    }

  protected String getFlowPlanPath()
    {
    return getRootPath() + "/pmml/dot/" + getTestName();
    }

  protected void pmmlTest( String testModel, Fields trainingFields, Fields predictorFields ) throws IOException
    {
    pmmlTest( testModel, trainingFields, predictorFields, null, null );
    }

  protected void pmmlTest( String testModel, Fields trainingFields, Fields predictorFields, Fields skipFields, Tuple[] skip ) throws IOException
    {
    IntermediateResults ir = pmmlTestCore( testModel, trainingFields, predictorFields );

    Fields sourceSelector = ir.getSource().getSourceFields().subtract( trainingFields );
    Fields sinkSelector = ir.getSink().getSinkFields();

    LOG.debug( "source select = {}", sourceSelector.printVerbose() );
    LOG.debug( "sink select   = {}", sinkSelector.printVerbose() );

    List<Tuple> sourceTuples = asList( ir.getFlow(), ir.getSource(), sourceSelector );
    List<Tuple> sinkTuples = asList( ir.getFlow(), ir.getSink(), sinkSelector );

    assertEquals( sourceTuples, sinkTuples, 0.000001d, skipFields, skip );
    }
  
  protected void pmmlTestAgainstExpected( String testModel, Fields trainingFields, Fields predictorFields ) throws IOException
    {
    IntermediateResults ir = pmmlTestCore( testModel, trainingFields, predictorFields );

    String expectedPath = DATA_PATH + testModel + ".expected.tsv";

    Fields sinkSelector = ir.getSink().getSinkFields();
    Tap<?, ?, ?> expectedTap = getPlatform().getDelimitedFile(sinkSelector, true, "\t", "\"", expectedPath, SinkMode.KEEP );

    LOG.debug( "expected      = {}", sinkSelector.printVerbose() );
    LOG.debug( "sink select   = {}", sinkSelector.printVerbose() );

    List<Tuple> expectedTuples = asList( ir.getFlow(), expectedTap, sinkSelector );
    List<Tuple> sinkTuples = asList( ir.getFlow(), ir.getSink(), sinkSelector );

    assertEquals( expectedTuples, sinkTuples, 0.000001d, null, null );

    ir.getFlow().cleanup();
    }

  private IntermediateResults pmmlTestCore( String testModel, Fields trainingFields, Fields predictorFields )
      throws FileNotFoundException
    {
    File file = new File( DATA_PATH + testModel + ".pmml" );

    assertTrue( "pmml file does not exist", file.exists() );

    PMMLPlanner planner = new PMMLPlanner()
      .setPMMLInput( new FileInputStream( file ) )
      .addDataTypes( predictorFields )
      .setDefaultPredictedField( new Fields( "predict", String.class ) );

    Pipe pipe = new Pipe( "head" );

    Fields discardFields = trainingFields.appendSelector( predictorFields );

    if( !discardFields.isNone() )
      pipe = new Discard( pipe, discardFields );

    Tap<?, ?, ?> source = getPlatform().getDelimitedFile( "\t", "\"", planner.getFieldTypeResolver(), DATA_PATH + testModel + ".tsv", SinkMode.KEEP );
    Tap<?, ?, ?> sink = getPlatform().getDelimitedFile( "\t", "\"", null, getResultPath(), SinkMode.REPLACE );

    FlowDef flowDef = FlowDef.flowDef()
      .addSource( "head", source )
      .addSink( "tail", sink )
      .addTail( pipe )
      .addAssemblyPlanner( planner );

    Flow<?> flow;

    try
      {
      flow = getPlatform().getFlowConnector().connect( flowDef );
      }
    catch( PlannerException exception )
      {
      exception.writeDOT( getFlowPlanPath() + "/plan.dot" );

      throw exception;
      }

    flow.writeDOT( getFlowPlanPath() + "/plan.dot" );

    flow.complete();

    LOG.debug( "source = {}", source.getSourceFields().printVerbose() );
    LOG.debug( "sink   = {}", sink.getSinkFields().printVerbose() );

    IntermediateResults ir = new IntermediateResults( source, sink, flow );
    return ir;
    }

    private static class IntermediateResults {
      private final Tap<?, ? , ?> source;
      private final Tap<?, ?, ?> sink;
      private final Flow<?> flow;
      
      public IntermediateResults( Tap<?, ?, ?> source, Tap<?, ?, ?> sink, Flow<?> flow)
        {
        super();
        this.source = source;
        this.sink = sink;
        this.flow = flow;
        }
      Tap<?, ?, ?> getSource()
        {
        return source;
        }
      Tap<?, ?, ?> getSink()
        {
        return sink;
        }
      Flow<?> getFlow()
        {
        return flow;
        }
      
    }
  }
