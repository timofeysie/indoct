package com.businessglue.temp;

import java.util.Vector;

public class Magazine {
   private String name;
   private Vector articles;

   public Magazine() {
      articles = new Vector();
   }

   public void setName( String rhs ) { name = rhs; }

   public void addArticle( Article a ) {
      articles.addElement( a );
   }

   public String toString() {
      StringBuffer buf = new StringBuffer( "Magazine: Name='" + name + "' ");
      for( int i=0; i<articles.size(); i++ ){
         buf.append( articles.elementAt(i).toString() );
      }
      return buf.toString();
   }
}
