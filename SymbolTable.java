import java.util.*;
public class SymbolTable {

 Hashtable < String, LinkedList < String >> st;
 Hashtable < String, String > table_type;
 Hashtable < String, String > table_desc;


 SymbolTable() {
  this.st = new Hashtable < > ();
  this.table_type = new Hashtable < > ();
  this.table_desc = new Hashtable < > ();

  st.put("global", new LinkedList < > ());
 }

 public void get(String id, String type, String scope) {
  LinkedList < String > list = st.get(scope);
  if (list == null) {
   System.out.println("Variable " + id + " not declared in " + scope);
  }
 }


 public void print() {
  Enumeration e = st.keys();
  while (e.hasMoreElements()) {
   String scope = (String) e.nextElement();
   System.out.println("Scope: " + scope);
   System.out.println("*************************");

   LinkedList < String > idList = st.get(scope);
   for (String id: idList) {
    String type = table_type.get(id + scope);
    String description = table_desc.get(id + scope);
    System.out.println("(" + id + ", " + type + ", " + description + ")");
   }
   System.out.println();
  }
 }

 public String type_check(String id, String scope) {
  String type = table_type.get(id + scope);
  if (type != null)
   return type;
  else {
   type = table_type.get(id + "global");
   if (type != null) {
    return type;
   }
  }
  return null;
 }

 public String get_paramtype(int index,String scope){int count=0;LinkedList<String>idList=st.get(scope);for(String id:idList){String type=table_type.get(id+scope);String description=table_desc.get(id+scope);if(description.equals("param")){count++;if(count==index){return type;}}}
return null;}
public String getDescription(String id,String scope){String description=table_desc.get(id+scope);if(description!=null)
return description;else{description=table_desc.get(id+"global");if(description!=null){return description;}}
return null;}
public LinkedList<String>getScopeTable(String scope){return st.get(scope);}
public int get_params(String id){LinkedList<String>list=st.get(id);int count=0;for(int i=0;i<list.size();i++){String description=table_desc.get(list.get(i)+id);if(description.equals("param")){count++;}}
return count;}

 public boolean dup_check(String id, String scope) {
  LinkedList < String > list = st.get(scope);
  LinkedList < String > global_list = st.get("global");
  if (scope.equals("global")) {
   return global_list.indexOf(id) == global_list.lastIndexOf(id);
  }
  return ((list.indexOf(id) == list.lastIndexOf(id)) && (global_list.indexOf(id) == -1));

 }



 public void put(String id, String type, String information, String scope) {
  LinkedList < String > list = st.get(scope);
  if (list == null) {
   list = new LinkedList < > ();

   list.add(id);

   st.put(scope, list);
  } else {

   list.addFirst(id);
  }

  table_type.put(id + scope, type);
  table_desc.put(id + scope, information);
 }


 public ArrayList < String > functionsToList() {
  LinkedList < String > list = st.get("global");
  ArrayList < String > functions = new ArrayList < String > ();
  for (int i = 0; i < list.size(); i++) {
   String description = table_desc.get(list.get(i) + "global");
   if (description.equals("function"))
    functions.add(list.get(i));
  }
  return functions;
 }

 public boolean func_check(String id) {
  LinkedList < String > list = st.get("global");
  ArrayList < String > functions = new ArrayList < String > ();
  for (int i = 0; i < list.size(); i++) {
   String description = table_desc.get(list.get(i) + "global");
   if (description.equals("function") && list.get(i).equals(id)) {
    return true;
   }
  }
  return false;
 }
}
