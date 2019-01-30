import java.util.*;
public class threeaddresscode implements calVisitor {
 private static int label_assign = 1;

 public Object visit(SimpleNode node, Object data) {
  throw new RuntimeException("Visit SimpleNode");
 }

 public Object visit(Program node, Object data) {
  int num = node.jjtGetNumChildren();
  for (int i = 0; i < num; i++) {
   node.jjtGetChild(i).jjtAccept(this, data);
  }
  return data;
 }

 public Object visit(Var node, Object data) {
  String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
  String type = (String) node.jjtGetChild(1).jjtAccept(this, data);
  if (node.jjtGetParent().toString().equals("Prog")) {
   System.out.println("VAR\t" + id);
  }
  return data;

 }
 public Object visit(ID node, Object data) {
  return node.value;
 }

 private void print_op(Statement node, Object data) {
  String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
  String result = id + " = ";
  for (int i = 1; i < node.jjtGetNumChildren() - 1; i++) {
   result += " " + node.jjtGetChild(i).jjtAccept(this, data);
  }
  System.out.println("\t\t" + result);
 }

 public Object visit(Const node, Object data) {
  String id = (String) node.jjtGetChild(0).jjtAccept(this, data);
  String type = (String) node.jjtGetChild(1).jjtAccept(this, data);
  String num = (String) node.jjtGetChild(2).jjtAccept(this, data);
  if (node.jjtGetParent().toString().equals("Program")) {
   System.out.println("CONST\t" + id + "\t=\t" + num);
  }
  return data;
 }

 public Object visit(Main node, Object data) {
  System.out.println("MAIN:");

  int num = node.jjtGetNumChildren();
  for (int i = 0; i < num; i++) {
   node.jjtGetChild(i).jjtAccept(this, data);
  }

  return data;
 }

 public Object visit(Function node, Object data) {
  String func_name = (String) node.jjtGetChild(1).jjtAccept(this, data);
  int num = node.jjtGetNumChildren();
  int bytes = node.jjtGetChild(2).jjtGetNumChildren() * 4;

  System.out.println(func_name.toUpperCase() + ":\t");

  String ret = "\t\treturn ";

  for (int i = 0; i < num; i++) {

   if (node.jjtGetChild(i).toString().equals("FunctionReturn")) {
    ret += node.jjtGetChild(i).jjtAccept(this, data);
   } else {
    node.jjtGetChild(i).jjtAccept(this, data);
   }
  }

  System.out.println(ret);


  return data;
 }

 public Object visit(Comparison node, Object data) {
  node.childrenAccept(this, data);
  return node.value;
 }



 public Object visit(FunctionReturn node, Object data) {
  int num = node.jjtGetNumChildren();
  for (int i = 0; i < num; i++) {
   node.jjtGetChild(i).jjtAccept(this, data);
  }
  return node.value;
 }

 public Object visit(Type node, Object data) {
  return node.value;
 }

 public Object visit(ParameterList node, Object data) {
  int num = node.jjtGetNumChildren();
  for (int i = 0; i < num; i++) {
   node.jjtGetChild(i).jjtAccept(this, data);
  }
  return data;
 }

 /*mod: // modulus function, returns parameter 1 mod parameter 2
 mx = getparam 1
 my = getparam 2
 mt1 = mx / my
 mt2 = mt1 * my
 mt3 = mx - mt2
 return mt3 */


  public Object visit(Statement node, Object data) {
     int next = 1;
     if (node.value != null)

     {
         if (node.value.equals("If") || node.value.equals("While")) {

             ArrayList < String > ids = new ArrayList < > ();
             ArrayList < String > comparisons = new ArrayList < > ();
             ArrayList < String > conditions = new ArrayList < > ();

             for (int i = 0; i < node.jjtGetNumChildren(); i++) {
                 String n = node.jjtGetChild(i).toString();


                 if (n.equals("FunctionReturn")) {
                     ids.add((String) node.jjtGetChild(i).jjtAccept(this, data));
                     next++;
                 } else if (n.equals("Comparison")) {


                     String value = (String) node.jjtGetChild(i).jjtGetChild(0).jjtAccept(this, data);
                     String comp = (String) node.jjtGetChild(i).jjtGetChild(1).jjtAccept(this, data);
                     String comparison = comp + " " + value;
                     comparisons.add(comparison);
                     next++;


                 } else if (n.equals("AND") || n.equals("OR"))

                 {
                     conditions.add((String) node.jjtGetChild(i).jjtAccept(this, data));
                     next++;
                 }
             }

             String result = "";
             for (int i = 0; i < ids.size(); i++) {
                 result += ids.get(i) + " ";
                 if (comparisons.size() > i)


                 {
                     result += comparisons.get(i);
                 }
                 if (conditions.size() > i)

                 {
                     result += " " + conditions.get(conditions.size() - i - 1) + " ";
                 }
             }
             System.out.println("\t" + node.value + "\t" + "(" + result.trim() + ")" + " goto label" + label_assign);
             System.out.println("label" + label_assign + ":");
             label_assign++;
         }
     }
     int num = node.jjtGetNumChildren();
     if (num > 0) {
         String childNode = node.jjtGetChild(next).toString();


         String id = (String) node.jjtGetChild(next - 1).jjtAccept(this, data);
         if (childNode.equals("FunctionReturn"))

         {
             int n = node.jjtGetChild(next).jjtGetNumChildren();
             if (n > 0)

             {
                 String func_name = (String) node.jjtGetChild(next).jjtAccept(this, data);

                 int children = node.jjtGetChild(next).jjtGetChild(next - 1).jjtGetNumChildren();
                 Node child = node.jjtGetChild(next).jjtGetChild(next - 1);
                 int param_count = 0;

                 for (int i = 0; i < children; i++) {
                     String param = (String) child.jjtGetChild(i).jjtAccept(this, data);
                     System.out.println("\t\tparam\t" + param);
                     param_count++;
                 }
                 System.out.println("\t\t" + id + "\t=   call " + func_name + ", " + param_count);
             } else {
                 print_op(node, data);
             }

         } else if

         (childNode.equals("ArgList")) {
             int children = node.jjtGetChild(next).jjtGetNumChildren();
             for (int i = 0; i < children; i++) {
                 String param = (String) node.jjtGetChild(next).jjtGetChild(i).jjtAccept(this, data);

             }
             System.out.println("\t\tgoto\t" + id);
         } else {
             String value = (String) node.jjtGetChild(next).jjtAccept(this, data);

             if (id != null && value != null) {
                 System.out.println("\t\t" + id + "\t= " + value);
             }
         }
     }
     return node.value;

 }

public Object visit(Assign node,Object data){return data;}
public Object visit(Plus node,Object data){return"+";}
public Object visit(Minus node,Object data){if(node.jjtGetNumChildren()>0){return"-"+node.jjtGetChild(0).jjtAccept(this,data);}else
return"-";}
public Object visit(Number node,Object data){return node.value;}
public Object visit(Boolean node,Object data){return node.value;}
public Object visit(Equals node,Object data){return node.value;}
public Object visit(NEQUAL node,Object data){return node.value;}
public Object visit(LT node,Object data){return node.value;}
public Object visit(LTE node,Object data){return node.value;}
public Object visit(GT node,Object data){return node.value;}
public Object visit(GTE node,Object data){return node.value;}
public Object visit(FuncAssign node,Object data){return data;}
public Object visit(Return node,Object data){return data;}
public Object visit(OR node,Object data){return node.value;}
public Object visit(AND node,Object data){return node.value;}
public Object visit(ArgList node,Object data){int num=node.jjtGetNumChildren();for(int i=0;i<num;i++){node.jjtGetChild(i).jjtAccept(this,data);}
return node.value;}}
