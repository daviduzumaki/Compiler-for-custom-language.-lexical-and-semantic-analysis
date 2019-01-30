import java.util.*;
public class TypeCheckVisitor implements calVisitor
{
    private static String scope = "global";
    private static Hashtable<String, LinkedHashSet<String>> duplicates = new Hashtable<>();
    private static HashSet<String> Functions_used = new HashSet<>();
    private static SymbolTable ST;

  public Object visit(SimpleNode node, Object data) {
     throw new RuntimeException("Visit SimpleNode");
  }

  private static void setSymbolTable(Object data) {
      ST = (SymbolTable)(data);
  }

  public Object visit(Program node, Object data) {
    setSymbolTable(data);
    int num = node.jjtGetNumChildren();
    for(int i = 0; i < num; i++) {
        node.jjtGetChild(i).jjtAccept(this, data);
    }
    dup_checker();
    functioninvoke_check();
    return data;
  }

  private void functioninvoke_check() {
    ArrayList<String> functions = ST.functionsToList();
    for(int i = 0; i < functions.size(); i++) {
        if(!Functions_used.contains(functions.get(i))) {
            System.out.println("Wait a minute: " + functions.get(i) + " is never invoked");
        }
    }
  }


  private static void duplicateCheck(String id, String scope) {
    if(!ST.dup_check(id, scope)) {
        HashSet<String> dups = duplicates.get(scope);
        if(dups == null) {
            LinkedHashSet<String> set = new LinkedHashSet<>();
            set.add(id);
            duplicates.put(scope, set);
        }
        else {
            dups.add(id);
        }
    }
    if(!ST.dup_check(id, "global")) {
        HashSet<String> dups = duplicates.get(scope);
        if(dups == null) {
            LinkedHashSet<String> set = new LinkedHashSet<>();
            set.add(id);
            duplicates.put(scope, set);
        }
        else {
            dups.add(id);
        }
    }
  }
  public Object visit(Var node, Object data) {
    String id = (String)node.jjtGetChild(0).jjtAccept(this, data);
    String type = (String) node.jjtGetChild(1).jjtAccept(this, data);
    duplicateCheck(id, scope);
    return data;

  }
  private static void dup_checker() {
      if(duplicates == null || duplicates.isEmpty()) {
          System.out.println("There are no duplicates in the program");
      }
      else {
        Enumeration e = duplicates.keys();
        while(e.hasMoreElements()) {
            String scope = (String) e.nextElement();
            LinkedHashSet<String> dups = duplicates.get(scope);
            Iterator it = dups.iterator();
            System.out.print("Wait a minute: You are declaring  [");
            while(it.hasNext()) {
               System.out.print(" " + it.next());
            }

            System.out.println(" ] multiple times in " + "[ "+ scope +" ]");
      }
    }
  }
  public Object visit(ID node, Object data) {
    return node.value;
  }

  public Object visit(Const node, Object data) {
    String id = (String)node.jjtGetChild(0).jjtAccept(this, data);
    duplicateCheck(id, scope);
    String type = (String) node.jjtGetChild(1).jjtAccept(this, data);
    return data;
  }

  public Object visit(Main node, Object data) {
    this.scope = "main";
    int num = node.jjtGetNumChildren();
    for(int i = 0; i < num; i++) {
        node.jjtGetChild(i).jjtAccept(this, data);
    }
    return data;
  }

  public Object visit(Function node, Object data) {
    this.scope = (String) node.jjtGetChild(1).jjtAccept(this, data);
    int num = node.jjtGetNumChildren();
    for(int i = 0; i < num; i++) {
        node.jjtGetChild(i).jjtAccept(this, data);
    }
    return data;
  }



  public Object visit(FunctionReturn node, Object data) {
    return node.value;
  }

  public Object visit(Type node, Object data) {
    return node.value;
  }

  public Object visit(ParameterList node, Object data) {
    int num = node.jjtGetNumChildren();
    for(int i = 0; i < num; i++) {
        node.jjtGetChild(i).jjtAccept(this, data);
    }
    return data;
  }

  public Object visit(Statement node,Object data){if(node.jjtGetNumChildren()>0){String id=(String)node.jjtGetChild(0).jjtAccept(this,data);if(ST.func_check(id)){Functions_used.add(id);}

if(isDeclared(id,scope))
{String type=ST.type_check(id,scope);String description=ST.getDescription(id,scope);if(description.equals("Const")){System.out.println("Wait a minute: "+id+" is a constant. You cannot redeclare.");}

else
{String rightnode=node.jjtGetChild(1).toString();if(type.equals("Integer")){if(rightnode.equals("Number"))
{node.jjtGetChild(1).jjtAccept(this,data);}
else if
(rightnode.equals("Boolean")){System.out.println("Wait a minute: Expected integer but got a Boolean");}
else if
(rightnode.equals("FuncReturn")){String func_name=(String)node.jjtGetChild(1).jjtAccept(this,data);if(!isDeclared(func_name,"global")&&!isDeclared(func_name,scope)){System.out.println(func_name+" is not declared");}
else if
(ST.func_check(func_name)){Functions_used.add(func_name);String func_return=ST.type_check(func_name,"global");

if
(!func_return.equals("integer")){System.out.println("Wait a minute: Expected return type of integer but got "+func_return);}
int num_args=ST.get_params(func_name);int actual_args=node.jjtGetChild(1).jjtGetChild(0).jjtGetNumChildren();

if
(num_args!=actual_args)
System.out.println("Wait a minute: Expected "+num_args+" parameters instead got "+actual_args);else if
(num_args==actual_args){Node arg_list=node.jjtGetChild(1).jjtGetChild(0);for(int i=0;i<arg_list.jjtGetNumChildren();i++){String arg=(String)arg_list.jjtGetChild(i).jjtAccept(this,data);

if
(isDeclared(arg,scope)){String arg_type=ST.type_check(arg,scope);String type_expected=ST.get_paramtype(i+1,func_name);if(!arg_type.equals(type_expected)){System.out.println("Wait a minute: "+arg+" is a "+arg_type+" expected type of "+type_expected);}}
else

{System.out.println("Wait a minute: "+arg+" is not declared in this scope");}}}}}}
else if

(type.equals("Boolean")){

if
(rightnode.equals("Boolean"))
{node.jjtGetChild(1).jjtAccept(this,data);}
else if

(rightnode.equals("Number")){System.out.println("Wait a minute: Expected boolean but integer");}
else if

(rightnode.equals("FunctionReturn")){String func_name=(String)node.jjtGetChild(1).jjtAccept(this,data);

if(!isDeclared(func_name,"global")){System.out.println(func_name+" is not declared");}


else{String func_return=ST.type_check(func_name,"global");

if(!func_return.equals("boolean")){System.out.println("Wait a minute: Expected boolean but got "+func_return);}}}}}}


else if(!isDeclared(id,scope)){System.out.println(id+" "+scope);System.out.println("Wait a minute: "+id+" needs to be declared before using it");}}
return data;}

  private static boolean isDeclared(String id, String scope) {
      LinkedList<String> list = ST.getScopeTable(scope);
      LinkedList<String> global_list = ST.getScopeTable("global");
      if(list != null) {
          if(!global_list.contains(id) && !list.contains(id)) {
              return false;
          }
      }
      return true;
  }

 public Object visit(Assign node,Object data){return data;}
public Object visit(Plus node,Object data){return"+";}
public Object visit(Minus node,Object data){return"-";}
public Object visit(FuncAssign node,Object data){return data;}
public Object visit(Return node,Object data){return data;}
public Object visit(Number node,Object data){return node.value;}
public Object visit(Boolean node,Object data){return node.value;}
public Object visit(Equals node,Object data){return node.value;}
public Object visit(NEQUAL node,Object data){return node.value;}
public Object visit(LT node,Object data){return node.value;}
public Object visit(LTE node,Object data){return node.value;}
public Object visit(GT node,Object data){return node.value;}
public Object visit(GTE node,Object data){return node.value;}
public Object visit(OR node,Object data){return node.value;}
public Object visit(AND node,Object data){return node.value;}
public Object visit(ArgList node,Object data){int num=node.jjtGetNumChildren();for(int i=0;i<num;i++){node.jjtGetChild(i).jjtAccept(this,data);}
return data;}
public Object visit(Comparison node,Object data){node.childrenAccept(this,data);return node.value;}}
