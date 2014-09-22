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

/**
 * A rule of the form A => C where A is an antecedent ItemSet and C is a
 * consequent ItemSet
 * 
 * @param <Item>
 *          The type of Item this rule understands
 */
public final class AssociationRule<Item> implements Serializable
  {
  private final String id;
  private final ItemSet<Item> antecedent;
  private final ItemSet<Item> consequent;
  private final double support;
  private final double confidence;
  private final double lift;
  private final double leverage;
  private final double affinity;

  /**
   * Creates an association rule with given antecedent, and consequent plus
   * several bits of data previously computed about this rule from training
   * data. The additional data (id, support, confidence, lift, leverage, and
   * affinity) are not used except when the Outputs class ranks rules.
   * 
   * @param id
   *          A unique id given to this rule and used only for reporting results
   *          from Outputs
   * @param antecedent
   *          The ItemSet that is the left hand side of the implication in this
   *          rule
   * @param consequent
   *          The ItemSet that is the right hand side of the implication in this
   *          rule
   * @param support
   *          Number of records that matched both the consequent and the
   *          antecedent divided by the total number of records [support(a&c) =
   *          n[a&c] / n]
   * @param confidence
   *          The support of this rule divided by the support of the antecedent
   *          [ confidence(a->c) = support(a&c) / support(a) ]
   * @param lift
   *          The confidence of this rule divided by the support [ lift(a&c) =
   *          confidence(a->c) / support(c) ]
   * @param leverage
   *          The difference between the observed frequency of A+C and the
   *          frequency that would be expected if A and C were independent. [
   *          leverage(a->c) = support(a&c)/(support(a) * support(c) ]
   * @param affinity
   *          A measure of the transactions that contain both the antecedent and
   *          consequent (intersect) compared to those that contain the
   *          antecedent or the consequent (union). [ affinity(a->c) =
   *          support(a&c) / [ support(a) + support(a) - support(a&c) ]
   */
  public AssociationRule( String id, ItemSet<Item> antecedent, ItemSet<Item> consequent, double support,
      double confidence, double lift, double leverage, double affinity )
    {
    super();
    this.id = id;
    this.antecedent = antecedent;
    this.consequent = consequent;
    this.support = support;
    this.confidence = confidence;
    this.lift = lift;
    this.leverage = leverage;
    this.affinity = affinity;
    }

  /**
   * Constructor used to make testing easier. The support, confidence, lift,
   * leverage, and affinity are set to 0
   *
   * @param id
   *          A unique id given to this rule and used only for reporting results
   *          from Outputs
   * @param antecedent
   *          The ItemSet that is the left hand side of the implication in this
   *          rule
   * @param consequent
   *          The ItemSet that is the right hand side of the implication in this
   *          rule
   */
  AssociationRule( String id, ItemSet<Item> antecedent, ItemSet<Item> consequent )
    {
    this( id, antecedent, consequent, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN );
    }

  /**
   * True if the antecedent of this rule is a subset of the specified items
   * 
   * @param items
   * @return
   */
  boolean antecedentMatches( ItemSet<Item> items )
    {
    return antecedent.isSubsetOf( items );
    }

  /**
   * True if the consequent of this rule is a subset of the specified items
   * 
   * @param items
   * @return
   */
  boolean consequentMatches( ItemSet<? extends Item> items )
    {
    return consequent.isSubsetOf( items );
    }

  @Override
  public String toString()
    {
    return antecedent + "->" + consequent;
    }

  @Override
  public int hashCode()
    {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( antecedent == null ) ? 0 : antecedent.hashCode() );
    result = prime * result + ( ( consequent == null ) ? 0 : consequent.hashCode() );
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
    AssociationRule<?> other = (AssociationRule<?>) obj;
    if( antecedent == null )
      {
      if( other.antecedent != null )
        return false;
      }
    else if( !antecedent.equals( other.antecedent ) )
      return false;
    if( consequent == null )
      {
      if( other.consequent != null )
        return false;
      }
    else if( !consequent.equals( other.consequent ) )
      return false;
    return true;
    }

  public String getId()
    {
    return id;
    }

  public ItemSet<Item> getAntecedent()
    {
    return antecedent;
    }

  public ItemSet<Item> getConsequent()
    {
    return consequent;
    }

  public double getSupport()
    {
    return support;
    }

  public double getConfidence()
    {
    return confidence;
    }

  public double getLift()
    {
    return lift;
    }

  public double getLeverage()
    {
    return leverage;
    }

  public double getAffinity()
    {
    return affinity;
    }

  }
