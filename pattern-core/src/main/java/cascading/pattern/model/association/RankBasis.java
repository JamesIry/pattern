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
import java.util.Comparator;

/**
 * The basis on which a list of AssociationRule is ranked by an Output
 */
public enum RankBasis implements Comparator<AssociationRule<?>>, Serializable
  {
  /**
   * Rank based on an AssocitionRule's support field
   */
  SUPPORT
    {
    @Override
    public double extractField( AssociationRule<?> rule )
      {
      return rule.getSupport();
      }
    },
  /**
   * Rank based on an AssocitionRule's confidence field
   */
  CONFIDENCE
    {
    @Override
    public double extractField( AssociationRule<?> rule )
      {
      return rule.getConfidence();
      }
    },
  /**
   * Rank based on an AssocitionRule's lift field
   */
  LIFT
    {
    @Override
    public double extractField( AssociationRule<?> rule )
      {
      return rule.getLift();
      }
    },
  /**
   * Rank based on an AssocitionRule's leverage field
   */
  LEVERAGE
    {
    @Override
    public double extractField( AssociationRule<?> rule )
      {
      return rule.getLeverage();
      }
    },
  /**
   * Rank based on an AssocitionRule's affinity field
   */
  AFFINITY
    {
    @Override
    public double extractField( AssociationRule<?> rule )
      {
      return rule.getAffinity();
      }
    };

  /**
   * Extract a single value from a rule in order to sort based on it
   * 
   * @param rule
   * @return
   */
  abstract double extractField( AssociationRule<?> rule );

  /**
   * Compares two AssociationRules based on this feature such that the rule with
   * the higher value comes first
   * 
   * @return
   */
  @Override
  public int compare( AssociationRule<?> o1, AssociationRule<?> o2 )
    {
    double d1 = extractField( o1 );
    double d2 = extractField( o2 );
    // we want higher values sorted earlier in lists
    return ( d1 > d2 ) ? -1 : ( ( d2 > d1 ) ? 1 : 0 );
    }
  }
