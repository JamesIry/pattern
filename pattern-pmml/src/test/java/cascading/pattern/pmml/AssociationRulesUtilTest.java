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

import static cascading.pattern.pmml.AssociationRulesUtil.convertOutputs;
import static cascading.pattern.pmml.AssociationRulesUtil.convertRankBasis;
import static cascading.pattern.pmml.AssociationRulesUtil.convertRuleFeature;
import static cascading.pattern.pmml.AssociationRulesUtil.convertRuleMatcher;
import static cascading.pattern.pmml.AssociationRulesUtil.convertRules;
import static cascading.pattern.pmml.AssociationRulesUtil.convertType;
import static cascading.pattern.pmml.AssociationRulesUtil.safeDouble;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.DataType;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.Item;
import org.dmg.pmml.ItemRef;
import org.dmg.pmml.Itemset;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.OutputField.Algorithm;
import org.dmg.pmml.RuleFeatureType;
import org.junit.Test;

import cascading.pattern.model.association.AssociationRule;
import cascading.pattern.model.association.ItemSet;
import cascading.pattern.model.association.Output;
import cascading.pattern.model.association.RankBasis;
import cascading.pattern.model.association.RuleFeature;
import cascading.pattern.model.association.RuleMatcher;

public class AssociationRulesUtilTest
  {
  @Test
  public void testSafeDouble()
    {
    assertEquals( 1.0, safeDouble( 1.0 ), 0.0 );
    assertEquals( Double.NaN, safeDouble( null ), 0.0 );
    }

  @Test
  public void testConvertRuleMatcher()
    {
    assertEquals( RuleMatcher.EXCLUSIVE_RECOMMENDATION, convertRuleMatcher( Algorithm.EXCLUSIVE_RECOMMENDATION ) );
    assertEquals( RuleMatcher.RECOMMENDATION, convertRuleMatcher( Algorithm.RECOMMENDATION ) );
    assertEquals( RuleMatcher.RULE_ASSOCIATION, convertRuleMatcher( Algorithm.RULE_ASSOCIATION ) );
    }

  @Test
  public void testConvertRankBasis()
    {
    assertEquals( RankBasis.AFFINITY, convertRankBasis( org.dmg.pmml.OutputField.RankBasis.AFFINITY ) );
    assertEquals( RankBasis.CONFIDENCE, convertRankBasis( org.dmg.pmml.OutputField.RankBasis.CONFIDENCE ) );
    assertEquals( RankBasis.LEVERAGE, convertRankBasis( org.dmg.pmml.OutputField.RankBasis.LEVERAGE ) );
    assertEquals( RankBasis.LIFT, convertRankBasis( org.dmg.pmml.OutputField.RankBasis.LIFT ) );
    assertEquals( RankBasis.SUPPORT, convertRankBasis( org.dmg.pmml.OutputField.RankBasis.SUPPORT ) );
    }

  @Test
  public void testConvertRuleFeature()
    {
    assertEquals( RuleFeature.AFFINITY, convertRuleFeature( RuleFeatureType.AFFINITY ) );
    assertEquals( RuleFeature.ANTECEDENT, convertRuleFeature( RuleFeatureType.ANTECEDENT ) );
    assertEquals( RuleFeature.CONFIDENCE, convertRuleFeature( RuleFeatureType.CONFIDENCE ) );
    assertEquals( RuleFeature.CONSEQUENT, convertRuleFeature( RuleFeatureType.CONSEQUENT ) );
    assertEquals( RuleFeature.ID, convertRuleFeature( RuleFeatureType.RULE_ID ) );
    assertEquals( RuleFeature.LEVERAGE, convertRuleFeature( RuleFeatureType.LEVERAGE ) );
    assertEquals( RuleFeature.LIFT, convertRuleFeature( RuleFeatureType.LIFT ) );
    assertEquals( RuleFeature.RULE, convertRuleFeature( RuleFeatureType.RULE ) );
    assertEquals( RuleFeature.SUPPORT, convertRuleFeature( RuleFeatureType.SUPPORT ) );
    }

  @Test
  public void testConvertType()
    {
    assertEquals( String.class, convertType( null ) );
    assertEquals( String.class, convertType( DataType.STRING ) );
    assertEquals( Double.class, convertType( DataType.DOUBLE ) );
    }

  @SuppressWarnings( "unchecked" )
  @Test
  public void testConvertOutputs()
    {
    OutputField pmmlField1 = new OutputField();
    pmmlField1.setName( new FieldName( "foo" ) );
    pmmlField1.setAlgorithm( Algorithm.RECOMMENDATION );
    pmmlField1.setRank( 1 );
    pmmlField1.setRankBasis( org.dmg.pmml.OutputField.RankBasis.AFFINITY );
    pmmlField1.setRuleFeature( RuleFeatureType.AFFINITY );
    pmmlField1.setDataType( DataType.STRING );

    Output<String> output1 = new Output<String>( "foo", RuleMatcher.RECOMMENDATION, 1, RankBasis.AFFINITY,
        RuleFeature.AFFINITY, String.class );

    OutputField pmmlField2 = new OutputField();
    pmmlField2.setName( new FieldName( "bar" ) );
    pmmlField2.setAlgorithm( Algorithm.EXCLUSIVE_RECOMMENDATION );
    pmmlField2.setRank( 2 );
    pmmlField2.setRankBasis( org.dmg.pmml.OutputField.RankBasis.CONFIDENCE );
    pmmlField2.setRuleFeature( RuleFeatureType.CONFIDENCE );
    pmmlField2.setDataType( DataType.DOUBLE );

    Output<String> output2 = new Output<String>( "bar", RuleMatcher.EXCLUSIVE_RECOMMENDATION, 2, RankBasis.CONFIDENCE,
        RuleFeature.CONFIDENCE, Double.class );

    org.dmg.pmml.Output pmmlOutput = new org.dmg.pmml.Output()
        .withOutputFields( Arrays.asList( pmmlField1, pmmlField2 ) );

    assertEquals( new HashSet<Output<String>>( Arrays.asList( output1, output2 ) ), convertOutputs( pmmlOutput ) );
    }

  @Test
  public void testConvertRules()
    {
    List<Item> items = Arrays.asList( new Item( "1",  "bar" ), new Item( "2", "baz"), new Item("3", "bar2"), new Item("4", "baz2") );
    
    ItemSet<String> itemSet1 = new ItemSet<String>( "bar", "baz" );
    ItemSet<String> itemSet2 = new ItemSet<String>( "bar2", "baz2" );
    
     Itemset itemset1 = new Itemset( "foo" ).withItemRefs( Arrays.asList( new ItemRef("1"), new ItemRef("2") ) );
     Itemset itemset2 = new Itemset( "foo2" ).withItemRefs( Arrays.asList( new ItemRef("3"), new ItemRef("4") ) );
     List<Itemset> itemsets = Arrays.asList( itemset1, itemset2 );

    AssociationRule<String> expectedNoId = new AssociationRule<String>( "1", itemSet1, itemSet2, 1.0, 2.0, Double.NaN,
        Double.NaN, Double.NaN );
    org.dmg.pmml.AssociationRule pmmlRuleNoId = new org.dmg.pmml.AssociationRule( "foo", "foo2", 1.0, 2.0 );

    org.dmg.pmml.AssociationRule pmmlRuleWithId = new org.dmg.pmml.AssociationRule( "foo", "foo2", 1.0, 2.0 );
    pmmlRuleWithId.setId( "blurp" );
    pmmlRuleWithId.setLift( 3.0 );
    pmmlRuleWithId.setLeverage( 4.0 );
    pmmlRuleWithId.setAffinity( 5.0 );
    
    List<org.dmg.pmml.AssociationRule> pmmlRules = Arrays.asList( pmmlRuleNoId, pmmlRuleWithId );
    
    AssociationRule<String> expectedId = new AssociationRule<String>( "blurp", itemSet1, itemSet2, 1.0, 2.0, 3.0,
        4.0, 5.0 );
    
    @SuppressWarnings( "unchecked" )
    Set<AssociationRule<String>> expected = new HashSet<AssociationRule<String>>(Arrays.asList( expectedNoId, expectedId ));
    
    assertEquals( expected, convertRules( pmmlRules, itemsets, items ) );
    }
  }
