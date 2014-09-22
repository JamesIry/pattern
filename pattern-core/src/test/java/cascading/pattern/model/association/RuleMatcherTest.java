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

import org.junit.Test;

import cascading.pattern.model.association.AssociationRule;
import cascading.pattern.model.association.ItemSet;
import cascading.pattern.model.association.RuleMatcher;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RuleMatcherTest
  {
  private ItemSet<String> crackerSet = new ItemSet<String>( "cracker" );
  private ItemSet<String> waterSet = new ItemSet<String>( "water" );
  private ItemSet<String> cokeSet = new ItemSet<String>( "coke" );
  private ItemSet<String> nachosSet = new ItemSet<String>( "nachos" );
  private ItemSet<String> crackerAndWater = new ItemSet<String>( "cracker", "water" );
  private ItemSet<String> pearAndBanana = new ItemSet<String>( "pear", "banana" );
  private ItemSet<String> crackerAndCoke = new ItemSet<String>( "cracker", "coke" );
  private ItemSet<String> waterAndCoke = new ItemSet<String>( "water", "coke" );
  private ItemSet<String> crackerWaterAndCoke = new ItemSet<String>( "cracker", "water", "coke" );
  private ItemSet<String> crackerWaterBananaAndApple = new ItemSet<String>( "cracker", "water", "banana", "apple" );

  private AssociationRule<String> rule1 = new AssociationRule<String>( "rule1", crackerSet, waterSet );
  private AssociationRule<String> rule2 = new AssociationRule<String>( "rule2", waterSet, crackerSet );
  private AssociationRule<String> rule3 = new AssociationRule<String>( "rule3", crackerSet, cokeSet );
  private AssociationRule<String> rule4 = new AssociationRule<String>( "rule4", crackerAndWater, nachosSet );
  private AssociationRule<String> rule5 = new AssociationRule<String>( "rule5", waterSet, pearAndBanana );

  @Test
  public void testRecommendation()
    {
    RuleMatcher matcher = RuleMatcher.RECOMMENDATION;

    verify( crackerAndCoke, matcher, 1, 3 );
    verify( crackerAndWater, matcher, 1, 2, 3, 4, 5 );
    verify( waterAndCoke, matcher, 2, 5 );
    verify( crackerWaterAndCoke, matcher, 1, 2, 3, 4, 5 );
    verify( crackerWaterBananaAndApple, matcher, 1, 2, 3, 4, 5 );
    }

  @Test
  public void testExclusiveRecommendation()
    {
    RuleMatcher matcher = RuleMatcher.EXCLUSIVE_RECOMMENDATION;

    verify( crackerAndCoke, matcher, 1 );
    verify( crackerAndWater, matcher, 3, 4, 5 );
    verify( waterAndCoke, matcher, 2, 5 );
    verify( crackerWaterAndCoke, matcher, 4, 5 );
    verify( crackerWaterBananaAndApple, matcher, 3, 4, 5 );
    }

  @Test
  public void testRuleAssociation()
    {
    RuleMatcher matcher = RuleMatcher.RULE_ASSOCIATION;

    verify( crackerAndCoke, matcher, 3 );
    verify( crackerAndWater, matcher, 1, 2 );
    verify( waterAndCoke, matcher );
    verify( crackerWaterAndCoke, matcher, 1, 2, 3 );
    verify( crackerWaterBananaAndApple, matcher, 1, 2 );
    }

  private void verify( ItemSet<String> items, RuleMatcher matcher, Integer... rulesRaw )
    {
    List<Integer> expected = Arrays.asList( rulesRaw );
    List<Integer> actual = new ArrayList<Integer>();
    if( matcher.matches( rule1, items ) )
      actual.add( 1 );
    if( matcher.matches( rule2, items ) )
      actual.add( 2 );
    if( matcher.matches( rule3, items ) )
      actual.add( 3 );
    if( matcher.matches( rule4, items ) )
      actual.add( 4 );
    if( matcher.matches( rule5, items ) )
      actual.add( 5 );
    assertEquals( expected, actual );
    }

  }