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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cascading.pattern.model.association.ItemSet;
import static org.junit.Assert.*;

public class OutputTest
  {
  private final ItemSet<String> set1 = new ItemSet<String>( "foo", "baz", "num" );
  private final ItemSet<String> set2 = new ItemSet<String>( "foo", "baz" );

  private final AssociationRule<String> rule1 = new AssociationRule<String>( "rule1", set1, set1, 100.0, 200.0, 300.0,
      4.0, 5.0 );
  private final AssociationRule<String> rule2 = new AssociationRule<String>( "rule2", set2, set2, 10.0, 2.0, 30.0,
      400.0, 50.0 );
  private final AssociationRule<String> rule3 = new AssociationRule<String>( "rule3", set1, set2, 1.0, 20.0, 3.0, 40.0,
      500.0 );

  @SuppressWarnings( "unchecked" )
  private final List<AssociationRule<String>> rules = Arrays.asList( rule1, rule2, rule3 );

  @Test
  public void testNullList()
    {
    Output<String> output = new Output<String>( "foo", RuleMatcher.TEST_MATCH_EVERYTHING, 1, RankBasis.AFFINITY,
        RuleFeature.AFFINITY, Double.class );
    List<AssociationRule<String>> list = null;
    Object result = output.extract( list );
    assertEquals( null, result );
    }

  @Test
  public void testRankTooHigh()
    {
    Output<String> output = new Output<String>( "foo", RuleMatcher.TEST_MATCH_EVERYTHING, 5, RankBasis.AFFINITY,
        RuleFeature.AFFINITY, Double.class );
    Object result = output.extract( new ArrayList<AssociationRule<String>>() );
    assertEquals( null, result );
    }

  @Test
  public void testBasic()
    {
    Output<String> output = new Output<String>( "foo", RuleMatcher.TEST_MATCH_EVERYTHING, 1, RankBasis.AFFINITY,
        RuleFeature.AFFINITY, Double.class );
    Object result = output.extract( rules );
    assertEquals( 500.0, result );
    }

  @Test
  public void testLowerRank()
    {
    Output<String> output = new Output<String>( "foo", RuleMatcher.TEST_MATCH_EVERYTHING, 2, RankBasis.AFFINITY,
        RuleFeature.AFFINITY, Double.class );
    Object result = output.extract( rules );
    assertEquals( 50.0, result );
    }

  @Test
  public void testDifferentFeature()
    {
    Output<String> output = new Output<String>( "foo", RuleMatcher.TEST_MATCH_EVERYTHING, 1, RankBasis.AFFINITY,
        RuleFeature.LEVERAGE, Double.class );
    Object result = output.extract( rules );
    assertEquals( 40.0, result );
    }

  @Test
  public void testDifferentRankBasis()
    {
    Output<String> output = new Output<String>( "foo", RuleMatcher.TEST_MATCH_EVERYTHING, 1, RankBasis.CONFIDENCE,
        RuleFeature.AFFINITY, Double.class );
    Object result = output.extract( rules );
    assertEquals( 5.0, result );
    }

  @Test
  public void testDifferentResultType()
    {
    Output<String> output = new Output<String>( "foo", RuleMatcher.TEST_MATCH_EVERYTHING, 1, RankBasis.CONFIDENCE,
        RuleFeature.AFFINITY, String.class );
    Object result = output.extract( rules );
    assertEquals( "5.0", result );
    }

  @Test
  public void testExtractMap()
    {
    Output<String> output = new Output<String>( "foo", RuleMatcher.TEST_MATCH_EVERYTHING, 1, RankBasis.AFFINITY,
        RuleFeature.AFFINITY, Double.class );
    Map<RuleMatcher, List<AssociationRule<String>>> map = new HashMap<RuleMatcher, List<AssociationRule<String>>>();
    map.put( RuleMatcher.TEST_MATCH_EVERYTHING, rules );
    Object result = output.extract( map );
    assertEquals( 500.0, result );
    }

  @Test
  public void testExtractMapMissingKey()
    {
    Output<String> output = new Output<String>( "foo", RuleMatcher.TEST_MATCH_EVERYTHING, 1, RankBasis.AFFINITY,
        RuleFeature.AFFINITY, Double.class );
    Map<RuleMatcher, List<AssociationRule<String>>> map = new HashMap<RuleMatcher, List<AssociationRule<String>>>();
    map.put( RuleMatcher.EXCLUSIVE_RECOMMENDATION, rules );
    Object result = output.extract( map );
    assertEquals( null, result );
    }

  }
