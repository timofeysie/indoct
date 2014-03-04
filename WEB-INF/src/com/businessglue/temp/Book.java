package com.businessglue.temp;

public class Book 
{
   private String author;
   private String title;

   public Book() {}

   public void setAuthor( String rhs ) { author = rhs; }
   public void setTitle(  String rhs ) { title  = rhs; }

   public String toString() 
   {
      return "Book: Author='" + author + "' Title='" + title + "'";
   }
}
