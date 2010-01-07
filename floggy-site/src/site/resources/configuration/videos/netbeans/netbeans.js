/*
 * Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var args = new Object();
var query = location.search.substring(1);

// Get query string
var pairs = query.split( "," );

// Break at comma
for ( var i = 0; i < pairs.length; i++ )
{
   var pos = pairs[i].indexOf('=');
   if( pos == -1 ) 
   {   
      continue; // Look for "name=value"
   }

   var argname  = pairs[i].substring( 0, pos );  // If not found, skip
   var value    = pairs[i].substring( pos + 1 ); // Extract the name
   args[argname] = unescape( value );            // Extract the value
} 








