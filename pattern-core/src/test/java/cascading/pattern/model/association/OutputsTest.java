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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OutputsTest
  {
  private final ItemSet<String> set1 = new ItemSet<String>( "foo", "baz", "num" );
  private final ItemSet<String> set2 = new ItemSet<String>( "foo", "baz" );

  private final AssociationRule<String> rule1 = new AssociationRule<String>( "rule1", set1, set1, 100.0, 200.0, 300.0,
      4.0, 5.0 );
  private final AssociationRule<String> rule2 = new AssociationRule<String>( "rule2", set2, set2, 10.0, 2.0, 30.0,
      400.0, 50.0 );
  private final AssociationRule<String> rule3 = new AssociationRule<String>( "rule3", set1, set2, 1.0, 20.0, 3.0, 40.0,
      500.0 );

  Output<String> output1 = new Output<String>( "output1", RuleMatcher.TEST_MATCH_EVERYTHING, 1, RankBasis.AFFINITY,
      RuleFeature.AFFINITY, Double.class );
  Output<String> output2 = new Output<String>( "output2", RuleMatcher.TEST_MATCH_EVERYTHING, 1, RankBasis.AFFINITY,
      RuleFeature.LEVERAGE, Double.class );
  Output<String> output3 = new Output<String>( "output3", RuleMatcher.TEST_MATCH_NOTHING, 1, RankBasis.AFFINITY,
      RuleFeature.LEVERAGE, Double.class );

  @SuppressWarnings( "unchecked" )
  private final List<AssociationRule<String>> rules = Arrays.asList( rule1, rule2, rule3 );

  @Test
  public void testRuleMatchers()
    {
    @SuppressWarnings( "unchecked" )
    Outputs<String> outputs = new Outputs<String>( new HashSet<Output<String>>( Arrays.asList( output1, output2,
        output3 ) ) );

    assertEquals(
        new HashSet<RuleMatcher>( Arrays.asList( RuleMatcher.TEST_MATCH_EVERYTHING, RuleMatcher.TEST_MATCH_NOTHING ) ),
        outputs.ruleMatchers() );
    }

  @Test
  public void testExtract()
    {
    @SuppressWarnings( "unchecked" )
    Outputs<String> outputs = new Outputs<String>( new HashSet<Output<String>>( Arrays.asList( output1, output2,
        output3 ) ) );

    Map<RuleMatcher, List<AssociationRule<String>>> map = new HashMap<RuleMatcher, List<AssociationRule<String>>>();
    map.put( RuleMatcher.TEST_MATCH_EVERYTHING, rules );

    Map<String, Object> results = outputs.extract( map );

    Map<String, Object> expected = new HashMap<String, Object>();
    expected.put( "output1", 500.0 );
    expected.put( "output2", 40.0 );
    expected.put( "output3", null );
    assertEquals( expected, results );
    }
  }
