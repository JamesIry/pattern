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

import org.junit.Test;

public class RankBasisTest
  {
  private final ItemSet<String> set1 = new ItemSet<String>( "foo", "baz", "num" );
  private final ItemSet<String> set2 = new ItemSet<String>( "foo", "baz" );

  private final AssociationRule<String> rule1 = new AssociationRule<String>( "rule1", set1, set1, 100.0, 200.0, 300.0,
      40.0, 50.0 );
  private final AssociationRule<String> rule2 = new AssociationRule<String>( "rule2", set2, set2, 10.0, 20.0, 30.0,
      400.0, 500.0 );

  @Test
  public void test()
    {
    verify( rule1, 100.0, rule2, 10.0, RankBasis.SUPPORT );
    verify( rule1, 200.0, rule2, 20.0, RankBasis.CONFIDENCE );
    verify( rule1, 300.0, rule2, 30.0, RankBasis.LIFT );
    verify( rule2, 400.0, rule1, 40.0, RankBasis.LEVERAGE );
    verify( rule2, 500.0, rule1, 50.0, RankBasis.AFFINITY );
    }

  private void verify( AssociationRule<String> rule1, double value1, AssociationRule<String> rule2, double value2,
      RankBasis basis )
    {
    assertEquals( value1, basis.extractField( rule1 ), 0.0 );
    assertEquals( value2, basis.extractField( rule2 ), 0.0 );
    assertEquals( -1, basis.compare( rule1, rule2 ) );
    assertEquals( 1, basis.compare( rule2, rule1 ) );
    }
  }
