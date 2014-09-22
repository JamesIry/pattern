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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import cascading.pattern.model.association.AssociationRule;
import cascading.pattern.model.association.ItemSet;
import static org.junit.Assert.*;

public class AssociationRulesTest
  {
  private ItemSet<String> set1 = new ItemSet<String>( "foo", "baz", "num" );
  private ItemSet<String> set2 = new ItemSet<String>( "foo", "baz" );

  AssociationRule<String> rule1 = new AssociationRule<String>( "rule1", set1, set1 );
  AssociationRule<String> rule2 = new AssociationRule<String>( "rule2", set2, set2 );
  AssociationRule<String> rule3 = new AssociationRule<String>( "rule3", set1, set2 );

  @SuppressWarnings( "unchecked" )
  AssociationRules<String> rules = new AssociationRules<String>( rule1, rule2, rule3 );

  @SuppressWarnings( "unchecked" )
  @Test
  public void testEvaluateSingleMatcher()
    {
    assertEquals( Arrays.asList( rule1, rule2, rule3 ), rules.evaluate( set1, RuleMatcher.TEST_MATCH_EVERYTHING ) );
    assertEquals( Arrays.asList(), rules.evaluate( set1, RuleMatcher.TEST_MATCH_NOTHING ) );
    }

  @SuppressWarnings( "unchecked" )
  @Test
  public void testEvaluateMultipleMatchers()
    {
    Set<RuleMatcher> matchers = new HashSet<RuleMatcher>( Arrays.asList( RuleMatcher.TEST_MATCH_EVERYTHING,
        RuleMatcher.TEST_MATCH_NOTHING ) );
    Map<RuleMatcher, List<AssociationRule<String>>> map = rules.evaluate( set1, matchers );
    assertEquals( 2, map.size() );
    assertEquals( Arrays.asList( rule1, rule2, rule3 ), map.get( RuleMatcher.TEST_MATCH_EVERYTHING ) );
    assertEquals( Arrays.asList(), map.get( RuleMatcher.TEST_MATCH_NOTHING ) );
    }
  }