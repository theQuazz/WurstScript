package de.peeeq.wurstscript.jasstranslation;

import static de.peeeq.wurstscript.jassAst.JassAst.JassExprBinary;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprBoolVal;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprFuncRef;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprFunctionCall;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprIntVal;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprNull;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprRealVal;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprStringVal;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprUnary;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprVarAccess;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprVarArrayAccess;
import static de.peeeq.wurstscript.jassAst.JassAst.JassExprlist;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpAnd;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpDiv;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpEquals;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpGreater;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpGreaterEq;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpLess;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpLessEq;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpMinus;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpMult;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpNot;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpOr;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpPlus;
import static de.peeeq.wurstscript.jassAst.JassAst.JassOpUnequals;
import static de.peeeq.wurstscript.jassAst.JassAst.JassStatements;
import static de.peeeq.wurstscript.jassAst.JassAst.JassStmtIf;
import static de.peeeq.wurstscript.jassAst.JassAst.JassStmtSet;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import de.peeeq.wurstscript.ast.AstElement;
import de.peeeq.wurstscript.ast.AstElementWithIndexes;
import de.peeeq.wurstscript.ast.ConstructorDef;
import de.peeeq.wurstscript.ast.Expr;
import de.peeeq.wurstscript.ast.ExprBinary;
import de.peeeq.wurstscript.ast.ExprBoolVal;
import de.peeeq.wurstscript.ast.ExprCast;
import de.peeeq.wurstscript.ast.ExprFuncRef;
import de.peeeq.wurstscript.ast.ExprFunctionCall;
import de.peeeq.wurstscript.ast.ExprIncomplete;
import de.peeeq.wurstscript.ast.ExprIntVal;
import de.peeeq.wurstscript.ast.ExprMemberArrayVar;
import de.peeeq.wurstscript.ast.ExprMemberMethod;
import de.peeeq.wurstscript.ast.ExprMemberVar;
import de.peeeq.wurstscript.ast.ExprNewObject;
import de.peeeq.wurstscript.ast.ExprNull;
import de.peeeq.wurstscript.ast.ExprRealVal;
import de.peeeq.wurstscript.ast.ExprStringVal;
import de.peeeq.wurstscript.ast.ExprThis;
import de.peeeq.wurstscript.ast.ExprUnary;
import de.peeeq.wurstscript.ast.ExprVarAccess;
import de.peeeq.wurstscript.ast.ExprVarArrayAccess;
import de.peeeq.wurstscript.ast.FunctionCall;
import de.peeeq.wurstscript.ast.FunctionDefinition;
import de.peeeq.wurstscript.ast.NameDef;
import de.peeeq.wurstscript.ast.NameRef;
import de.peeeq.wurstscript.ast.OpAnd;
import de.peeeq.wurstscript.ast.OpBinary;
import de.peeeq.wurstscript.ast.OpDivInt;
import de.peeeq.wurstscript.ast.OpDivReal;
import de.peeeq.wurstscript.ast.OpEquals;
import de.peeeq.wurstscript.ast.OpGreater;
import de.peeeq.wurstscript.ast.OpGreaterEq;
import de.peeeq.wurstscript.ast.OpLess;
import de.peeeq.wurstscript.ast.OpLessEq;
import de.peeeq.wurstscript.ast.OpMinus;
import de.peeeq.wurstscript.ast.OpModInt;
import de.peeeq.wurstscript.ast.OpModReal;
import de.peeeq.wurstscript.ast.OpMult;
import de.peeeq.wurstscript.ast.OpNot;
import de.peeeq.wurstscript.ast.OpOr;
import de.peeeq.wurstscript.ast.OpPlus;
import de.peeeq.wurstscript.ast.OpUnary;
import de.peeeq.wurstscript.ast.OpUnequals;
import de.peeeq.wurstscript.ast.VarDef;
import de.peeeq.wurstscript.ast.WParameter;
import de.peeeq.wurstscript.ast.WParameters;
import de.peeeq.wurstscript.attributes.CompileError;
import de.peeeq.wurstscript.jassAst.JassExpr;
import de.peeeq.wurstscript.jassAst.JassExprBinary;
import de.peeeq.wurstscript.jassAst.JassExprlist;
import de.peeeq.wurstscript.jassAst.JassFunction;
import de.peeeq.wurstscript.jassAst.JassOpBinary;
import de.peeeq.wurstscript.jassAst.JassOpUnary;
import de.peeeq.wurstscript.jassAst.JassStatement;
import de.peeeq.wurstscript.jassAst.JassStatements;
import de.peeeq.wurstscript.jassAst.JassVar;
import de.peeeq.wurstscript.types.PScriptTypeInt;
import de.peeeq.wurstscript.types.PscriptType;
import de.peeeq.wurstscript.types.PscriptTypeClass;
import de.peeeq.wurstscript.types.PscriptTypeInterface;

public class JassTranslatorExpressions {
	private JassManager manager;
	private JassTranslator translator;

	public JassTranslatorExpressions(JassTranslator t) {
		this.translator = t;
		this.manager = t.manager;
	}

	protected ExprTranslationResult translateExpr(final JassFunction f, Expr expr) {
		translator.lastElement = expr;
		
		
		return expr.match(new Expr.Matcher<ExprTranslationResult>() {

			@Override
			public ExprTranslationResult case_ExprNewObject(ExprNewObject exprNewObject) {
				ConstructorDef constructorFunc = exprNewObject.attrConstructorDef();
				JassFunction constructorJassFunc = manager.getJassConstructorFor(constructorFunc);
				translator.calledFunctions.put(f, constructorJassFunc);

				JassExprlist arguments = JassExprlist(); 
				ExprListTranslationResult args = translateArguments(exprNewObject, f, exprNewObject.getArgs(), exprNewObject.attrFunctionSignature().getParamTypes());
				List<JassStatement> statements = Lists.newLinkedList();
				statements.addAll(args.getStatements());
				arguments.addAll(args.getExprs());
				JassExpr e = JassExprFunctionCall(constructorJassFunc.getName(), arguments);
				return new ExprTranslationResult(statements, e);
			}

			@Override
			public ExprTranslationResult case_ExprRealVal(ExprRealVal exprRealVal) {
				return new ExprTranslationResult(JassExprRealVal(exprRealVal.getValR()));
			}

			@Override
			public ExprTranslationResult case_ExprUnary(ExprUnary exprUnary) {
				ExprTranslationResult right = translateExpr(f, exprUnary.getRight());

				assert right.getExpressions().size() == 1;

				return new ExprTranslationResult(
						right.getStatements(), 
						JassExprUnary(translateOpUnary(exprUnary.getOpU()), right.getExpressions().get(0)));
			}

			@Override
			public ExprTranslationResult case_ExprThis(ExprThis exprThis) {
				return new ExprTranslationResult(JassExprVarAccess("this"));
			}

			@Override
			public ExprTranslationResult case_ExprCast(ExprCast exprCast) {
				// a cast has no effect:
				return translateExpr(f, exprCast.getExpr());
			}

			@Override
			public ExprTranslationResult case_ExprFunctionCall(ExprFunctionCall exprFunctionCall) {
				return translateFunctionCall(f, exprFunctionCall);
			}

			@Override
			public ExprTranslationResult case_ExprMemberMethod(ExprMemberMethod e) {
				return translateFunctionCall(f, e);
			}


			@Override
			public ExprTranslationResult case_ExprMemberArrayVar(ExprMemberArrayVar e) {
				return translateVarAccess(f, e);
			}

			@Override
			public ExprTranslationResult case_ExprStringVal(ExprStringVal exprStringVal) {
				return new ExprTranslationResult(JassExprStringVal(exprStringVal.getValS()));
			}

			@Override
			public ExprTranslationResult case_ExprVarAccess(ExprVarAccess e) {
				return translateVarAccess(f, e);
			}

			@Override
			public ExprTranslationResult case_ExprBinary(ExprBinary exprBinary) {
				ExprTranslationResult left = translateExpr(f, exprBinary.getLeft());
				ExprTranslationResult right = translateExpr(f, exprBinary.getRight());

				JassExpr leftExpr;
				JassExpr rightExpr;

				List<JassStatement> statements = Lists.newLinkedList();
				

				PscriptType leftType = exprBinary.getLeft().attrTyp();
				PscriptType rightType = exprBinary.getRight().attrTyp();
				String[] leftTypes = translator.translateType(leftType);
				String[] rightTypes = translator.translateType(rightType);
				
				if (left.getExpressions().size() > 1 || right.getExpressions().size() > 1) {
					statements.addAll(right.getStatements());
					
					if (left.exprCount() != right.exprCount()) {
						if (leftType instanceof PscriptTypeClass && rightType instanceof PscriptTypeInterface) {
							int instanceId = translator.getInstanceId(exprBinary, (PscriptTypeInterface)rightType, (PscriptTypeClass)leftType);
							left = left.plus(JassExprIntVal(instanceId));
							leftTypes = rightTypes;
						} else if (rightType instanceof PscriptTypeClass && leftType instanceof PscriptTypeInterface) {
							int instanceId = translator.getInstanceId(exprBinary, (PscriptTypeInterface)leftType, (PscriptTypeClass)rightType);
							right = right.plus(JassExprIntVal(instanceId));
							rightTypes = leftTypes;
						} else {
							throw new CompileError(exprBinary.getSource(), "incompatible types, " + leftType + " and " + rightType);
						}
					}
					
					
					JassVar[] tempLeft = translator.assignToTempVar(f, statements, leftTypes, left);
					JassVar[] tempRight = translator.assignToTempVar(f, statements, rightTypes, right);
					if (exprBinary.getOp() instanceof OpEquals) {
						JassExprBinary e = JassExprBinary(JassExprVarAccess(tempLeft[0].getName()), JassOpEquals(), JassExprVarAccess(tempRight[0].getName()));
						for (int i=1; i < left.exprCount(); i++) {
							e = 
									JassExprBinary(
											e, JassOpAnd()
											, JassExprBinary(JassExprVarAccess(tempLeft[i].getName())
													, translateOp(exprBinary.getOp())
													, JassExprVarAccess(tempRight[i].getName())));
						}
						
						return new ExprTranslationResult(statements, e);
					} else {
						throw new CompileError(exprBinary.getSource(), "Invalid binary operator.");
					}
				}
				

				statements.addAll(left.getStatements());

				// if the right hand side of the expression uses statements we have to make sure that
				// the left hand side is executed first:
				if (right.getStatements().size() > 0) {
					String type = translator.translateTypeSingle(leftType);
					JassVar tempVar = translator.getNewTempVar(f, type);
					statements.add(JassStmtSet(tempVar.getName(), left.getExpressions().get(0)));
					leftExpr = JassExprVarAccess(tempVar.getName());


					// boolean operators (and, or) have to be treated differently because the evaluation
					// of the right hand side depends on the result of the left hand side.
					if (exprBinary.getOp() instanceof OpAnd) {
						JassStatements thenBlock = JassStatements();
						JassStatements elseBlock  = JassStatements();;
						elseBlock.addAll(right.getStatements());
						elseBlock.add(JassStmtSet(tempVar.getName(), right.getExpressions().get(0)));
						statements.add(
								JassStmtIf(
										JassExprVarAccess(tempVar.getName()), 
										thenBlock, elseBlock));
						return new ExprTranslationResult(statements, JassExprVarAccess(tempVar.getName()));
					} else if (exprBinary.getOp() instanceof OpAnd) {
						JassStatements thenBlock = JassStatements();
						JassStatements elseBlock  = JassStatements();;
						thenBlock.addAll(right.getStatements());
						thenBlock.add(JassStmtSet(tempVar.getName(), right.getExpressions().get(0)));
						statements.add(
								JassStmtIf(
										JassExprVarAccess(tempVar.getName()), 
										thenBlock, elseBlock));
						return new ExprTranslationResult(statements, JassExprVarAccess(tempVar.getName()));
					} else {
						rightExpr = right.getExpressions().get(0);
					}
				} else {
					leftExpr = left.getExpressions().get(0);
					rightExpr = right.getExpressions().get(0);
				}

				JassExpr e;

				// modulo operators nead special treatment ...
				if (exprBinary.getOp() instanceof OpModInt) {
					e = JassExprFunctionCall("ModuloInteger", JassExprlist(leftExpr, rightExpr));
				} else if (exprBinary.getOp() instanceof OpModReal) {
					e = JassExprFunctionCall("ModuloReal", JassExprlist(leftExpr, rightExpr));
				} else if (exprBinary.getOp() instanceof OpDivReal 
						&& leftType instanceof PScriptTypeInt
						&& exprBinary.getRight().attrTyp() instanceof PScriptTypeInt) {
					// multiply the left expression by 1.0 to convert it to real
					e = JassExprBinary(JassExprBinary(leftExpr, JassOpMult(), JassExprRealVal(1.0)), JassOpDiv(), rightExpr);
				} else {
					e = JassExprBinary(leftExpr, translateOp(exprBinary.getOp()), rightExpr);
				}
				return new ExprTranslationResult(statements, e);
			}

			@Override
			public ExprTranslationResult case_ExprBoolVal(ExprBoolVal exprBoolVal) {
				return new ExprTranslationResult(JassExprBoolVal(exprBoolVal.getValB()));
			}

			@Override
			public ExprTranslationResult case_ExprMemberVar(ExprMemberVar e) {
				return translateVarAccess(f, e);
			}

			@Override
			public ExprTranslationResult case_ExprNull(ExprNull exprNull) {
				// FIXME null oder 0 ?
				List<JassExpr> exprs = Lists.newArrayList();
				String[] types = exprNull.attrTyp().jassTranslateType();
				for (String type : types) {
					if (type.equals("integer")) {
						exprs.add(JassExprIntVal(0));
					} else {
						exprs.add(JassExprNull());
					}
				}
				return new ExprTranslationResult(Collections.<JassStatement>emptyList(), exprs);
				
			}

			@Override
			public ExprTranslationResult case_ExprIntVal(ExprIntVal exprIntVal) {
				return new ExprTranslationResult(JassExprIntVal(exprIntVal.getValI()));
			}

			@Override
			public ExprTranslationResult case_ExprFuncRef(ExprFuncRef exprFuncRef) {
				FunctionDefinition funcDef = exprFuncRef.attrFuncDef();
				JassFunction jassFunc = manager.getJassFunctionFor(funcDef);

				// f calls jassfunc
				translator.calledFunctions.put(f, jassFunc);

				String funcName = jassFunc.getName();
				return new ExprTranslationResult(JassExprFuncRef(funcName));
			}

			@Override
			public ExprTranslationResult case_ExprVarArrayAccess(ExprVarArrayAccess e) {
				return translateVarAccess(f, e);
			}

			@Override
			public ExprTranslationResult case_ExprIncomplete(
					ExprIncomplete exprIncomplete) {
				throw new CompileError(exprIncomplete.getSource(), exprIncomplete.getErrorMessage());
			}
		});
	}

	protected ExprTranslationResult translateVarAccess(JassFunction f, NameRef e) {
		NameDef decl = e.attrNameDef();
		if (decl instanceof VarDef) {
			VarDef varDef = (VarDef) decl;

			List<JassVar> jassVars = manager.getJassVarsFor(varDef);

			if (e.attrImplicitParameter() instanceof Expr) {
				// we have implicit parameter
				// e.g. "someObject.someField"
				if (e instanceof AstElementWithIndexes) {
					throw new CompileError(e.getSource(), "Member array variables are not supported.");
				} else {
					ExprTranslationResult index1 = translateExpr(f, (Expr) e.attrImplicitParameter());
					return createVarArrayAccess(jassVars, index1);
				}
			} else {
				// direct var access
				if (e instanceof AstElementWithIndexes) {
					// direct access array var
					AstElementWithIndexes withIndexes = (AstElementWithIndexes) e;
					if (withIndexes.getIndexes().size() > 1) {
						throw new CompileError(e.getSource(), "More than one index is not supported.");
					}
					ExprTranslationResult index1 = translateExpr(f, withIndexes.getIndexes().get(0));
					return createVarArrayAccess(jassVars, index1);
				} else {
					// not an array var
					return createVarAccess(jassVars);

				}
			}
		} else {
			throw new CompileError(e.getSource(), "Cannot translate reference to " + decl.getClass().getName());
		}
	}

	private ExprTranslationResult createVarAccess(List<JassVar> jassVars) {
		JassExpr[] exprs = new JassExpr[jassVars.size()];
		for (int i = 0; i < jassVars.size(); i++) {
			String jassVarName = jassVars.get(i).getName();
			exprs[i] = JassExprVarAccess(jassVarName);
		}
		return new ExprTranslationResult(exprs);
	}

	private ExprTranslationResult createVarArrayAccess(List<JassVar> jassVars, ExprTranslationResult index1) {
		JassExpr[] exprs = new JassExpr[jassVars.size()];
		for (int i = 0; i < exprs.length; i++) {
			String jassVarName = jassVars.get(i).getName();
			JassExpr indexExpr = index1.getExpressions().get(0);
			if (i > 0) {
				// TODO if we use the index expression several times there could
				// be duplicated side effects
				indexExpr = (JassExpr) indexExpr.copy();
			}
			exprs[i] = JassExprVarArrayAccess(jassVarName, indexExpr);
		}
		return new ExprTranslationResult(index1.getStatements(), exprs);
	}

	/**
	 * translate a list of expressions, makes sure that the arguments are
	 * evaluated in correct order so if we hava a list of expressions (a,b,c)
	 * and c requires additional statements then a and b will use statements too
	 * 
	 * @param f
	 */
	private ExprListTranslationResult translateArguments(AstElement where, JassFunction f, List<Expr> args,
			List<PscriptType> types) {
		// if (args.size() != types.size()) {
		// throw new IllegalArgumentException("argsize = " + args.size() +
		// " but typessize = " + types.size() + " // " + f.getName());
		// }

		List<ExprTranslationResult> translations = Lists.newLinkedList();
		int lastTranslationWithStatements = 0;
		int i = 0;
		for (Expr arg : args) {
			ExprTranslationResult translation = translateExpr(f, arg);
			if (translation.getStatements().size() > 0) {
				lastTranslationWithStatements = i;
			}
			translations.add(translation);
			i++;
		}

		List<JassStatement> statements = Lists.newLinkedList();
		List<JassExpr> exprs = Lists.newLinkedList();

		if (translations.size() != types.size()) {
			throw new CompileError(where.attrSource(), "Argument size = " + translations.size() + " but " + "types size = "
					+ types.size());
		}

		i = 0;
		for (ExprTranslationResult arg : translations) {
			statements.addAll(arg.getStatements());

			PscriptType paramType = types.get(i);
			String[] paramJassTypes = translator.translateType(paramType);

			if (paramJassTypes.length == 2 && arg.exprCount() == 1) {
				Expr a = args.get(i);
				if (paramType instanceof PscriptTypeInterface && a.attrTyp() instanceof PscriptTypeClass) {
					int typeId = translator.getInstanceId(a, (PscriptTypeInterface) paramType, (PscriptTypeClass) a.attrTyp());
					arg = arg.plus(JassExprIntVal(typeId));
				} else if (a.attrTyp() instanceof PScriptTypeInt
						|| a.attrTyp() instanceof PscriptTypeClass) {
					arg = arg.plus(JassExprIntVal(0));
				} else {
					throw new CompileError(a.getSource(), "Cannot pass " + a.attrTyp() + ", expected " + paramType);
				}
			}

			for (int j = 0; j < arg.exprCount(); j++) {
				if (i < lastTranslationWithStatements) {
					JassVar tempVar = translator.getNewTempVar(f, paramJassTypes[j]);
					statements.add(JassStmtSet(tempVar.getName(), arg.getExpressions().get(j)));
					exprs.add(JassExprVarAccess(tempVar.getName()));
				} else {
					exprs.add(arg.getExpressions().get(j));
				}
			}
			i++;
		}
		return new ExprListTranslationResult(statements, exprs);
	}

	private JassOpUnary translateOpUnary(OpUnary op) {
		return op.match(new OpUnary.Matcher<JassOpUnary>() {

			@Override
			public JassOpUnary case_OpNot(OpNot opNot) {
				return JassOpNot();
			}

			@Override
			public JassOpUnary case_OpMinus(OpMinus opMinus) {
				return JassOpMinus();
			}
		});
	}

	private JassOpBinary translateOp(OpBinary op) {
		return op.match(new OpBinary.Matcher<JassOpBinary>() {

			@Override
			public JassOpBinary case_OpDivInt(OpDivInt opDivInt) {
				return JassOpDiv();
			}

			@Override
			public JassOpBinary case_OpLessEq(OpLessEq opLessEq) {
				return JassOpLessEq();
			}

			@Override
			public JassOpBinary case_OpEquals(OpEquals opEquals) {
				return JassOpEquals();
			}

			@Override
			public JassOpBinary case_OpModReal(OpModReal opModReal) {
				throw new Error("modulo operator cannot be translated");
			}

			@Override
			public JassOpBinary case_OpModInt(OpModInt opModInt) {
				throw new Error("modulo operator cannot be translated");
			}

			@Override
			public JassOpBinary case_OpUnequals(OpUnequals opUnequals) {
				return JassOpUnequals();
			}

			@Override
			public JassOpBinary case_OpDivReal(OpDivReal opDivReal) {
				return JassOpDiv();
			}

			@Override
			public JassOpBinary case_OpAnd(OpAnd opAnd) {
				return JassOpAnd();
			}

			@Override
			public JassOpBinary case_OpGreater(OpGreater opGreater) {
				return JassOpGreater();
			}

			@Override
			public JassOpBinary case_OpPlus(OpPlus opPlus) {
				return JassOpPlus();
			}

			@Override
			public JassOpBinary case_OpMult(OpMult opMult) {
				return JassOpMult();
			}

			@Override
			public JassOpBinary case_OpGreaterEq(OpGreaterEq opGreaterEq) {
				return JassOpGreaterEq();
			}

			@Override
			public JassOpBinary case_OpMinus(OpMinus opMinus) {
				return JassOpMinus();
			}

			@Override
			public JassOpBinary case_OpLess(OpLess opLess) {
				return JassOpLess();
			}

			@Override
			public JassOpBinary case_OpOr(OpOr opOr) {
				return JassOpOr();
			}
		});
	}

	List<PscriptType> getParameterTypes(WParameters params) {
		List<PscriptType> result = Lists.newArrayListWithCapacity(params.size());
		for (WParameter p : params) {
			result.add(p.attrTyp());
		}
		return result;
	}

	private ExprTranslationResult translateFunctionCall(JassFunction f, FunctionCall call) {
		List<Expr> arguments = Lists.newLinkedList(call.getArgs());
		if (call.attrImplicitParameter() instanceof Expr) {
			// add implicit parameter to front
			arguments.add(0, (Expr) call.attrImplicitParameter());
		}

		FunctionDefinition calledFunc = call.attrFuncDef();
		if (calledFunc == null) {
			// this must be an ignored function
			return new ExprTranslationResult(JassExprNull());
		}
		JassFunction calledJassFunc = manager.getJassFunctionFor(calledFunc);

		translator.calledFunctions.put(f, calledJassFunc);

		String functionName = calledJassFunc.getName();
		JassExprlist jassArgs = JassExprlist();
		List<JassStatement> statements = Lists.newLinkedList();

		// translate arguments:
		ExprListTranslationResult args = translateArguments(call, f, arguments, call.attrParameterTypes());
		statements.addAll(args.getStatements());
		jassArgs.addAll(args.getExprs());

		List<JassExpr> exprs = Lists.newArrayList();
		exprs.add(JassExprFunctionCall(functionName, jassArgs));

		String[] returnTypes = translator.translateType(calledFunc.attrTyp());
		for (int i = 1; i < returnTypes.length; i++) {
			exprs.add(JassExprVarAccess(manager.getTupleReturnVar(returnTypes[i], i).getName()));
			i++;
		}
		return new ExprTranslationResult(statements, exprs);
	}


}