package de.fosd.typechef.parser.c
import org.anarres.cpp.Main

import de.fosd.typechef.featureexpr._
import de.fosd.typechef.parser._
import java.io.FileWriter
import java.io.File
import junit.framework._
import junit.framework.Assert._

import java.io.PrintStream
import java.io.FileOutputStream
import java.io.BufferedOutputStream

object MyUtil {
    implicit def runnable(f: () => Unit): Runnable =
        new Runnable() { def run() = f() }
}

object ParserMain {
    def parserMain(filePath: String, parentPath: String): AST =
        parserMain(filePath, parentPath, new CTypeContext())

        val p=new CParser()
    def parserMain(filePath: String, parentPath: String, initialContext: CTypeContext): AST = {
        val logStats = MyUtil.runnable(() => {
            if (TokenWrapper.profiling) {
                val statistics = new PrintStream(new BufferedOutputStream(new FileOutputStream(filePath + ".stat")))
                LineInformation.printStatistics(statistics)
                statistics.close()
            }
        })

        Runtime.getRuntime().addShutdownHook(new Thread(logStats))

        val result = p.translationUnit(
            CLexer.lexFile(filePath, parentPath).setContext(initialContext), FeatureExpr.base)

        println(printParseResult(result, FeatureExpr.base))
//        checkParseResult(result, FeatureExpr.base)

        //        val resultStr: String = result.toString
        //        println("FeatureSolverCache.statistics: " + FeatureSolverCache.statistics)
        //        val writer = new FileWriter(filePath + ".ast")
        //        writer.write(resultStr);
        //        writer.close
        //        println("done.")

        //XXX: that's too simple, we need to typecheck also split results.
        // Moreover it makes the typechecker crash currently (easily workaroundable though).
        result match {
            case p.Success(ast, _) => ast
            case _ => null
        }
    }

    def printParseResult(result: p.MultiParseResult[Any], feature: FeatureExpr): String = {
        result match {
            case p.Success(ast, unparsed) => {
                if (unparsed.atEnd)
                    (feature.toString + "\tsucceeded\n")
                else
                    (feature.toString + "\tstopped before end (at " + unparsed.first.getPosition + ")\n")
            }
            case p.NoSuccess(msg, unparsed, inner) =>
                (feature.toString + "\tfailed " + msg + "\n")
            case p.SplittedParseResult(f, left, right) => {
                printParseResult(left, feature.and(f)) + "\n" +
                    printParseResult(right, feature.and(f.not))
            }
        }
    }

    def checkParseResult(result: p.MultiParseResult[Any], feature: FeatureExpr) {
        result match {
            case p.Success(ast, unparsed) => {
                if (!unparsed.atEnd)
                    new Exception("parser did not reach end of token stream with feature " + feature + " (" + unparsed.first.getPosition + "): " + unparsed).printStackTrace
                //succeed
            }
            case p.NoSuccess(msg, unparsed, inner) =>
                new Exception(msg + " at " + unparsed + " with feature " + feature +" " + inner).printStackTrace
            case p.SplittedParseResult(f, left, right) => {
                checkParseResult(left, feature.and(f))
                checkParseResult(right, feature.and(f.not))
            }
        }
    }

    def main(args: Array[String]) = {
        for (filename <- args) {
            println("**************************************************************************")
            println("** Processing file: " + filename)
            println("**************************************************************************")
            val parentPath = new File(filename).getParent()
            parserMain(filename, parentPath, new CTypeContext())
            println("**************************************************************************")
            println("** End of processing for: " + filename)
            println("**************************************************************************")
        }
    }
}
