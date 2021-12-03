package com.revature.p1.orm.util.accessor;

import java.lang.reflect.Method;

public abstract class Accessor {

   private String name;
   private Method accessor;

   protected Accessor(String name, Method accessor) {
      this.name = name;
      this.accessor = accessor;
   }

   public String getName() { return this.name; }
   protected void setName(String name) { this.name = name; }
   public Method getAccessor() { return this.accessor; }
   protected void setAccessor(Method accessor) { this.accessor = accessor; }

}
