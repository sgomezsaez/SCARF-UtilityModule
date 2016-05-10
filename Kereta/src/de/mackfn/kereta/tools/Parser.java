package de.mackfn.kereta.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import de.mackfn.kereta.tools.Calculation.CalcTypes;

public class Parser {

	private Map<String, Integer> ops; 
	private List<String> bracketOpen;
	private List<String> bracketClose;
	
	public Parser()
	{
		bracketOpen = new ArrayList<String>();
		bracketOpen.add("(");
		bracketOpen.add("{");
	
		bracketClose = new ArrayList<String>();
		bracketClose.add(")");
		bracketClose.add("}");
		
		ops = new HashMap<String, Integer>();
		/* Operators */
		ops.put("+", 1);
		ops.put("-", 1);
		ops.put("*", 2);
		ops.put("/", 2);
		ops.put("^", 3);		

		/* Boolean */
		ops.put("SQU", 6);
		ops.put("SQR", 6);
		ops.put("ROT", 6);
		ops.put("ORR", 6);
		ops.put("XOR", 6);
		ops.put("AND", 6);
		ops.put("<", 6);
		ops.put(">", 6);
		ops.put("EQU", 6);
		ops.put("LEQ", 6);
		ops.put("BEQ", 6);
		ops.put("NOT", 6);
		ops.put("MOD", 6);
		
		/* Functions */
		ops.put("IFF", 6);
		ops.put("IFE", 6);
		ops.put("SUM", 6);
		ops.put("PCT", 6);
		ops.put("MIN", 6);
		ops.put("MAX", 6);
		ops.put("IGR", 6);
		ops.put("FCT", 6);
		ops.put("FAC", 6);
	}

	public int getOpValue(String token)
	{
		if (isBracketOpen(token)) return 0;
		
		String tok = token;
		if (token.contains("_")) tok = token.substring(0, token.indexOf('_'));
		return ops.get(tok);
	}

	public boolean isBracketOpen(String token)
	{
		return bracketOpen.contains(token);
	}
	
	public boolean isBracketClose(String token)
	{
		return bracketClose.contains(token);
	}
	
	public boolean isOp(String token)
	{
		if (isBracketOpen(token) || isBracketClose(token)) return true;
		
		String tok = token;
		if (token.contains("_")) tok = token.substring(0, token.indexOf('_'));
		return ops.containsKey(tok);
	}
	
	public static String getIndex(String token)
	{
		return token.substring(token.indexOf('_') + 1);
	}
	
	public static String getFirstIndex(String token)
	{
		StringTokenizer itms = new StringTokenizer(token, "_");
		if (itms.countTokens() == 2) {
			itms.nextToken();
			String idxs = itms.nextToken();
			if (idxs.length() > 0) return idxs.substring(0, 1);
		}
		return "";
	}
	
	public static boolean hasIndex(String token, String index)
	{
		StringTokenizer itms = new StringTokenizer(token, "_");
		if (itms.countTokens() == 2) {
			itms.nextToken();
			String idxs = itms.nextToken();
			return idxs.contains(index);
		}
		return false;
	}
	
	public boolean isShortToken(String input, int index) 
	{
		boolean result = false;
		
		String fst = input.substring(index, index + 1);
		if (this.isBracketOpen(fst)) result = true;
		else if (this.isBracketClose(fst)) result = true;
		else if (this.isOp(fst)) result = true;
	
		return result;
	}
	
	public boolean isLongOp(String input, int index) 
	{
		if (index + 2 >= input.length()) return false;
		return this.isOp(input.substring(index, index + 3));
	}
	
	public List<String> tokenizer(String input)
	{
		input = input.replaceAll("(\\r|\\n)", " ");
		
		String temp = "";
		String build = "";
		int pointer = 0;
		while (pointer < input.length())
		{
			boolean isShortTok = isShortToken(input, pointer);
			boolean isLongOp = isLongOp(input, pointer);
			boolean isComma = (input.substring(pointer, pointer + 1).equals(","));
			boolean isToken = (isShortTok || isLongOp || isComma);
			boolean isDeli = (input.substring(pointer, pointer + 1).equals(" "));
			
			if (isToken || isDeli) 
			{
				build += " " + temp;
				temp = "";
			}
			
			if (isShortTok || isComma)
			{
				build += " " + input.substring(pointer, pointer + 1);
				pointer++;
			}
			else if (isLongOp)
			{
				if (input.substring(pointer, pointer + 3).equals("SUM") || input.substring(pointer, pointer + 3).equals("IGR")) 
				{
					build += " " + input.substring(pointer, pointer + 5);
					pointer = pointer + 5;
				}
				else
				{
					build += " " + input.substring(pointer, pointer + 3);
					pointer = pointer + 3;
				}
			}
			else 
			{
				if (!isDeli) temp += input.substring(pointer, pointer + 1);
				pointer++;
			}
		}
		build += " " + temp;
		build = build.trim();
		input = build;
		
		List<String> result = new ArrayList<String>();
		StringTokenizer tokens = new StringTokenizer(input, " ");
		while (tokens.hasMoreTokens())
		{
			String token = tokens.nextToken();
			result.add(token);
		}
		return result;
	}

	@SuppressWarnings("static-access")
	public Calculation getCalculation(List<String> rpn) throws Exception 
	{
		Calculation result = null;
		Stack<Calculation> stack = new Stack<Calculation>();
		
		for (int i = 0; i < rpn.size(); i++)
		{
			String t = rpn.get(i);
			if (StaticTools.isDigit(t))
			{
				Double val = Double.parseDouble(t);
				Calculation cal = new Calculation(Calculation.CalcTypes.DIGIT);
				cal.setValue(val);
				stack.push(cal);
				result = cal;
			}
			else if (!isOp(t))
			{
				Calculation cal = new Calculation(Calculation.CalcTypes.VAR);
				cal.setVariable(t);
				stack.push(cal);
				result = cal;
			}
			else
			{
				CalcTypes type = StaticTools.getType(t);
				
				if (type == null) 
				{
					System.out.println("unknown token: " + t);
					throw new Exception();
				}
				
				Calculation cal = new Calculation(type);
				
				if (type == CalcTypes.SUM || type == CalcTypes.INTGR) {
					String index = this.getIndex(t);
					cal.setVariable(index);
				}
				else if (type == CalcTypes.FCT) 
				{
					String assignment = stack.pop().getVariable();
					String fct = stack.pop().getVariable();
					cal.setRemoteFunction(fct);
					cal.setAssignment(assignment);
				}
				
				int nbr = cal.getParamters().length;
				for (int p = nbr - 1; p >= 0; p--) 
				{
					cal.setParameter(stack.pop(), p);
				}
				stack.push(cal);
				result = cal;
			}
		}
		
		return result;
	}
		
	public List<String> getRPN(List<String> tokens)
	{
		List<String> result = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		
		for (int i = 0; i < tokens.size(); i++)
		{
			String t = tokens.get(i);
			if (!isOp(t) && !t.equals(",")) result.add(t);
			else 
			{
				if (stack.isEmpty() || isBracketOpen(t)) stack.push(t);
				else if (isBracketClose(t) || t.equals(","))
				{
					boolean pop = true;
					while (pop)
					{
						String last = stack.pop();
						if (isBracketClose(t) && isBracketOpen(last)) pop = false;
						else if (t.equals(",") && isBracketOpen(last)) {
							pop = false;
							stack.push(last);
						}
						else result.add(last);	
					}
				}
				else
				{
					String last = stack.peek();
					if (getOpValue(last) < getOpValue(t)) stack.push(t);
					else
					{
						boolean goon = true;
						while (goon)
						{
							if (stack.isEmpty()) goon = false;
							else
							{
								last = stack.peek();
								if (getOpValue(last) >= getOpValue(t)) 
								{
									result.add(stack.pop());
								}
								else goon = false;
							}
						}
						stack.push(t);
					}
				}
			}
		}
		while (!stack.isEmpty()) result.add(stack.pop());
		
		return result;
	}
}
