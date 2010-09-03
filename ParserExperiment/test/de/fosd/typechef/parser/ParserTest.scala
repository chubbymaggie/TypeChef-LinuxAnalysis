package de.fosd.typechef.parser

import junit.framework._;
import junit.framework.Assert._
import scala.util.parsing.combinator._

class ParserTest extends TestCase {
	
	def t(text:String):Token = t(text,0)
	def t(text:String, feature:Int):Token = new Token(text,feature)
	
	def testParseSimple(){
		//"(3+5)*(4+2+1)"
		val input=List(t("("),t("1"),t("+"),t("5"),t(")"),t("*"),t("("),t("4"),t("+"),t("2"),t("+"),t("1"),t(")"))
		val out=new ExprParser().parse(input)
		System.out.println(out)
		assertEquals( Mul(Plus(Lit(1),Lit(5)),Plus(Lit(4),Plus(Lit(2),Lit(1)))),out.get)
		
	} 
	
//	def testFirstFeatureOnLiteral(){
//		//"(3+5)*(4+2+1)"
//		val input=List(t("("),t("2",1),t("1",-1),t("+"),t("5"),t(")"),t("*"),t("("),t("4"),t("+"),t("2"),t("+"),t("1"),t(")"))
//		val out=new ExprParser().parse(input)
//		System.out.println(out)
//		assertEquals( Mul(Plus(Lit(1),Lit(5)),Plus(Lit(4),Plus(Lit(2),Lit(1)))),out.get)
//		
//	}
	
	def testMultiParseSimple(){
		val input=List(t("1"),t("+"),t("5"))
		val out=new MyMultiFeatureParser().parse(input)
		System.out.println(out)
	}
	def testMultiParseAlternative(){
		val input=List(t("1",1),t("2",-1),t("+"),t("5"))
		val out=new MyMultiFeatureParser().parse(input)
		System.out.println(out)
		
	}
	
//	def testParseDisiplined(){
//		val out=new ExprParser().parse("IFDEF (3+5) ELSE 3 ENDIF *(IFDEF 4 ELSE 12 ENDIF +2)")
//		System.out.println(out)
//		assertEquals(out.toString,"[1.53] parsed: Mul(IF(Plus(Lit(3),Lit(5)),Lit(3)),Plus(IF(Lit(4),Lit(12)),Lit(2)))")
//	} 
//	def testParseUndisiplined(){
//		val out=new ExprParser().parse("IFDEF (3+5 ELSE (3 ENDIF ) *(IFDEF 4 ELSE 12 ENDIF +2)")
//		System.out.println(out)
//		assertEquals(out.toString,"[1.53] parsed: Mul(IF(Plus(Lit(3),Lit(5)),Lit(3)),Plus(IF(Lit(4),Lit(12)),Lit(2)))")
//	} 
	
	
}