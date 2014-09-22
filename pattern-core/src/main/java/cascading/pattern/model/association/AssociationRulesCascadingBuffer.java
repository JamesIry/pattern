/*
 * Copyright (c) 2014 Concurrent, Inc. All Rights Reserved.
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
package cascading.pattern.model.association;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Buffer;
import cascading.operation.BufferCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

public final class AssociationRulesCascadingBuffer<FieldId extends Comparable<FieldId>, Item> extends
    BaseOperation<Void> implements Buffer<Void>
  {
  private final Outputs<FieldId> outputs;
  private final AssociationRulesEvaluator<FieldId, Item> evaluator;
  private final FieldId itemField;

  public AssociationRulesCascadingBuffer( Fields groupByFields, FieldId itemField, Type itemType,
      Set<AssociationRule<Item>> rules, Outputs<FieldId> outputs )
    {
    super( 1, Fields.join( groupByFields, outputs.fields() ) );
    this.evaluator = new AssociationRulesEvaluator<FieldId, Item>( rules, outputs );
    this.itemField = itemField;
    this.outputs = outputs;

    }

  @Override
  public void operate( @SuppressWarnings( "rawtypes" ) FlowProcess flowProcess, BufferCall<Void> bufferCall )
    {

    Iterator<TupleEntry> it = bufferCall.getArgumentsIterator();
    List<Item> itemList = new ArrayList<Item>();
    while( it.hasNext() )
      {
      TupleEntry entry = it.next();

      @SuppressWarnings( "unchecked" )
      Item item = (Item) entry.getObject( itemField );
      itemList.add( item );
      }

    ItemSet<Item> items = new ItemSet<Item>( itemList );

    Map<FieldId, Object> fieldValues = evaluator.evaluate( items );

    Tuple result = new Tuple();

    TupleEntry group = bufferCall.getGroup();
    result.addAll( group.getTuple() );

    for( Output<FieldId> output : outputs.outputs() )
      {
      Object value = fieldValues.get( output.getFieldId() );
      result.add( value );
      }
    bufferCall.getOutputCollector().add( result );
    }

  }
