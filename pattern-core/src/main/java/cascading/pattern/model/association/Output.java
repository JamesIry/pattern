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

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cascading.tuple.coerce.Coercions;

/**
 * Specification of a single output field expected from evaluating ItemSets
 * against AssociationRules.
 *
 * @param <FieldId>
 */
public final class Output<FieldId extends Comparable<FieldId>> implements Serializable
  {
  private final FieldId fieldId;
  private final int rank;
  private final RuleMatcher matcher;
  private final RankBasis rankBasis;
  private final RuleFeature ruleFeature;
  private final Type type;

  /**
   * Creates a single Output specification
   * 
   * @param fieldId
   *          unique id for the field to be produced
   * @param matcher
   *          the algorithm to be used to score ItemSets for this output
   * @param rank
   *          when an ItemSet is compared to a set of AssociationRules the
   *          result will be a List of matching AssociationRules. This parameter
   *          says which rule to pick for output. Rank 1 = first rule, 2 = 2nd
   *          rule, etc.
   * @param rankBasis
   *          specifies which of the rule's fields should be used to rank
   *          matching rules
   * @param ruleFeature
   *          The field to be extracted from the chosen rule
   * @param type
   *          Specifies the type into which the output will be converted (e.g. a
   *          String or Double)
   */
  public Output( FieldId fieldId, RuleMatcher matcher, int rank, RankBasis rankBasis, RuleFeature ruleFeature, Type type )
    {
    super();
    this.fieldId = fieldId;
    this.rank = rank;
    this.matcher = matcher;
    this.ruleFeature = ruleFeature;
    this.rankBasis = rankBasis;
    this.type = type;
    }

  /**
   * Extract a feature from a map that contains RuleMatcher/AssociationRule list
   * pairs Calls extract on the list of rules that match the RuleMatcher in this
   * Output
   * 
   * @param map
   * @return
   */
  public <Item> Object extract( Map<RuleMatcher, List<AssociationRule<Item>>> map )
    {
    List<AssociationRule<Item>> rules = map.get( matcher );
    return extract( rules );
    }

  /**
   * Extract a feature from a list of AssociationRule
   * 
   * @param rules
   * @return
   */
  public <Item> Object extract( List<AssociationRule<Item>> rules )
    {
    if( rules == null || rules.size() < rank )
      {
      return null;
      }
    else
      {
      Collections.sort( rules, rankBasis );
      int listPosition = rank - 1;
      Object raw = ruleFeature.extract( rules.get( listPosition ) );
      return Coercions.coerce( raw, type );
      }
    }

  FieldId getFieldId()
    {
    return fieldId;
    }

  RuleMatcher getMatcher()
    {
    return matcher;
    }

  @Override
  public int hashCode()
    {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( fieldId == null ) ? 0 : fieldId.hashCode() );
    return result;
    }

  @Override
  public boolean equals( Object obj )
    {
    if( this == obj )
      return true;
    if( obj == null )
      return false;
    if( getClass() != obj.getClass() )
      return false;
    Output<?> other = (Output<?>) obj;
    if( fieldId == null )
      {
      if( other.fieldId != null )
        return false;
      }
    else if( !fieldId.equals( other.fieldId ) )
      return false;
    return true;
    }

  @Override
  public String toString()
    {
    return "Output [fieldId=" + fieldId + ", rank=" + rank + ", matcher=" + matcher + ", rankBasis=" + rankBasis
        + ", ruleFeature=" + ruleFeature + ", type=" + type + "]";
    }

  public Type getType()
    {
    return type;
    }

  }
