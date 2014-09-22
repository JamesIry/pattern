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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dmg.pmml.DataType;
import org.dmg.pmml.Item;
import org.dmg.pmml.ItemRef;
import org.dmg.pmml.Itemset;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.OutputField.Algorithm;
import org.dmg.pmml.RuleFeatureType;

import cascading.pattern.model.association.AssociationRule;
import cascading.pattern.model.association.ItemSet;
import cascading.pattern.model.association.Output;
import cascading.pattern.model.association.RankBasis;
import cascading.pattern.model.association.RuleFeature;
import cascading.pattern.model.association.RuleMatcher;

/**
 * Utilities for converting from org.dmg.pmml model to
 * cascading.pattern.model.association model
 */
public class AssociationRulesUtil
  {
  /**
   * Set of Pattern Output based on the pmml Output spec
   */
  public static Set<Output<String>> convertOutputs( org.dmg.pmml.Output pmmlOutput )
    {
    Set<Output<String>> outputs = new LinkedHashSet<Output<String>>( pmmlOutput.getOutputFields().size() );
    for( OutputField pmmlField : pmmlOutput.getOutputFields() )
      {
      outputs.add( convertOutput( pmmlField ) );
      }
    return outputs;
    }

  /**
   * One Pattern Output based on a single pmml OutputField
   */
  static Output<String> convertOutput( OutputField pmmlField )
    {
    String name = pmmlField.getName().getValue();
    RuleMatcher matcher = convertRuleMatcher( pmmlField.getAlgorithm() );
    int rank = pmmlField.getRank();
    RankBasis rankBasis = convertRankBasis( pmmlField.getRankBasis() );
    RuleFeature ruleFeature = convertRuleFeature( pmmlField.getRuleFeature() );
    Type type = convertType( pmmlField.getDataType() );

    return new Output<String>( name, matcher, rank, rankBasis, ruleFeature, type );
    }

  /**
   * Convert a pmml DataType into a Type. Only a few DataTypes are supported
   * because only a few are used in output formatting
   */
  static Type convertType( DataType pmmlDataType )
    {
    if( pmmlDataType == null )
      pmmlDataType = DataType.STRING;
    switch( pmmlDataType )
      {
      case DOUBLE:
        return Double.class;
      case STRING:
        return String.class;
      default:
        throw new UnsupportedOperationException( "Unusupported data type " + pmmlDataType );
      }
    }

  /**
   * Convert pmml RuleFeatureType to pattern RuleFeature
   */
  static RuleFeature convertRuleFeature( RuleFeatureType pmmlRuleFeature )
    {
    switch( pmmlRuleFeature )
      {
      case AFFINITY:
        return RuleFeature.AFFINITY;
      case ANTECEDENT:
        return RuleFeature.ANTECEDENT;
      case CONFIDENCE:
        return RuleFeature.CONFIDENCE;
      case CONSEQUENT:
        return RuleFeature.CONSEQUENT;
      case LEVERAGE:
        return RuleFeature.LEVERAGE;
      case LIFT:
        return RuleFeature.LIFT;
      case RULE:
        return RuleFeature.RULE;
      case RULE_ID:
        return RuleFeature.ID;
      case SUPPORT:
        return RuleFeature.SUPPORT;
      default:
        throw new UnsupportedOperationException( "Unsupported rule feature type " + pmmlRuleFeature );
      }
    }

  /**
   * Convert pmml RankBasis to pattern RankBasis
   */
  static RankBasis convertRankBasis( org.dmg.pmml.OutputField.RankBasis pmmlRankBasis )
    {
    switch( pmmlRankBasis )
      {
      case AFFINITY:
        return RankBasis.AFFINITY;
      case CONFIDENCE:
        return RankBasis.CONFIDENCE;
      case LEVERAGE:
        return RankBasis.LEVERAGE;
      case LIFT:
        return RankBasis.LIFT;
      case SUPPORT:
        return RankBasis.SUPPORT;
      default:
        throw new UnsupportedOperationException( "Unsupported rank basis " + pmmlRankBasis );
      }
    }

  /**
   * Convert pmml Algorithm to pattern RuleMatcher
   */
  static RuleMatcher convertRuleMatcher( Algorithm pmmlAlgorithm )
    {
    switch( pmmlAlgorithm )
      {
      case EXCLUSIVE_RECOMMENDATION:
        return RuleMatcher.EXCLUSIVE_RECOMMENDATION;
      case RECOMMENDATION:
        return RuleMatcher.RECOMMENDATION;
      case RULE_ASSOCIATION:
        return RuleMatcher.RULE_ASSOCIATION;
      default:
        throw new UnsupportedOperationException( "Unsupported algorithm " + pmmlAlgorithm );

      }
    }

  /**
   * Convert List of pmml AssociationRule to Set of pattern AssociationRule pmml
   * separates the definition of item sets from their use in rules
   */
  public static Set<AssociationRule<String>> convertRules( List<org.dmg.pmml.AssociationRule> pmmlRules,
      List<Itemset> pmmlItemsets, List<Item> pmmlItems )
    {
    Map<String, String> items = new HashMap<String, String>( pmmlItems.size() );
    for( Item pmmlItem : pmmlItems )
      {
      items.put( pmmlItem.getId(), pmmlItem.getValue() );
      }

    // convert a map from itemset id to pattern ItemSet
    Map<String, ItemSet<String>> itemSets = new HashMap<String, ItemSet<String>>( pmmlItemsets.size() );
    for( Itemset pmmlItemSet : pmmlItemsets )
      {
      ItemSet<String> itemSet = convertItemSet( pmmlItemSet, items );
      itemSets.put( pmmlItemSet.getId(), itemSet );
      }

    // convert the rules using the ItemSets already convertd
    Set<AssociationRule<String>> rules = new LinkedHashSet<AssociationRule<String>>( pmmlRules.size() );
    int ordinal = 1;
    for( org.dmg.pmml.AssociationRule pmmlRule : pmmlRules )
      {
      rules.add( convertRule( ordinal, pmmlRule, itemSets ) );
      ordinal++;
      }
    return rules;
    }

  /**
   * Create a pattern ItemSet from a jpmml Itemset
   */
  static ItemSet<String> convertItemSet( Itemset pmmlItemSet, Map<String, String> itemValues )
    {
    Set<String> items = new LinkedHashSet<String>( pmmlItemSet.getItemRefs().size() );
    for( ItemRef itemRef : pmmlItemSet.getItemRefs() )
      {
      items.add( itemValues.get( itemRef.getItemRef() ) );
      }
    return new ItemSet<String>( items );
    }

  /**
   * Create a pattern AssociationRule from pmml AssociationRule and a Map from
   * item set id to pattern ItemSet
   */
  static AssociationRule<String> convertRule( int ordinal, org.dmg.pmml.AssociationRule pmmlRule,
      Map<String, ItemSet<String>> itemSets )
    {
    String id = pmmlRule.getId();
    if( id == null || id.isEmpty() )
      id = "" + ordinal;
    double support = safeDouble( pmmlRule.getSupport() );
    double confidence = safeDouble( pmmlRule.getConfidence() );
    double lift = safeDouble( pmmlRule.getLift() );
    double leverage = safeDouble( pmmlRule.getLeverage() );
    double affinity = safeDouble( pmmlRule.getAffinity() );
    ItemSet<String> antecedent = itemSets.get( pmmlRule.getAntecedent() );
    ItemSet<String> consequent = itemSets.get( pmmlRule.getConsequent() );
    return new AssociationRule<String>( id, antecedent, consequent, support, confidence, lift, leverage, affinity );
    }

  /**
   * Take a missing double and turn it into NaN.
   */
  static double safeDouble( Double value )
    {
    return value == null ? Double.NaN : value;
    }
  }
