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
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A set of Item that can be used in an AssociationRule as antecedent,
 * consequent, or input for scoring
 * 
 * @param <Item>
 *          The type of item held by this set
 */
public class ItemSet<Item> implements Serializable, Iterable<Item>
  {
  /**
   * Internal storage for the Items in this set
   */
  // use linked hash set so that toString returns a result consistent with what
  // the user entered
  private final Set<Item> items = new LinkedHashSet<Item>();

  /**
   * Construct an ItemSet from explicit arguments
   * 
   * @param items
   */
  public ItemSet( Item... items )
    {
    this( Arrays.asList( items ) );
    }

  /**
   * Construct an ItemSet from a collection
   * 
   * @param items
   */
  public ItemSet( Iterable<? extends Item> items )
    {
    for( Item item : items )
      {
      this.items.add( item );
      }
    }

  @Override
  public Iterator<Item> iterator()
    {
    return items.iterator();
    }

  /**
   * True if this ItemSet is a subset of the specified other subset
   * 
   * @param other
   * @return
   */
  public boolean isSubsetOf( ItemSet<? extends Item> other )
    {
    return other.items.containsAll( this.items );
    }

  @Override
  public String toString()
    {
    StringBuilder result = new StringBuilder();
    result.append( "{" );

    boolean first = true;
    for( Item item : items )
      {
      if( first )
        first = false;
      else
        result.append( ", " );
      result.append( item );
      }

    result.append( "}" );

    return result.toString();
    }

  @Override
  public int hashCode()
    {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( items == null ) ? 0 : items.hashCode() );
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
    ItemSet<?> other = (ItemSet<?>) obj;
    if( items == null )
      {
      if( other.items != null )
        return false;
      }
    else if( !items.equals( other.items ) )
      return false;
    return true;
    }
  }
