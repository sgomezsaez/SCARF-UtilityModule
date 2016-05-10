package de.mackfn.kereta.tools;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.mackfn.kereta.RESTFunction;

public class Calculation {

	public enum CalcTypes {
		ADD, SUB, MUL, DIV,
		BIGR, SMLR, EQU, ESML, EBIG,
		SQR, POW, SQROOT, ROOT,
		MIN, MAX, FAC,
		DIGIT, VAR,
		IF, IFE, 
		AND, OR, XOR, NOT,
		MOD,
		SUM, PRCT,
		INTGR,
		FCT
	}
	
	private CalcTypes type;
	private Calculation[] parameters;
	private double value;
	private String variable;
	private Map<String, String> assignment;
	private String remoteFct;
	private Calculation remoteObject;
	int midpointN = 1000;
	
	public Calculation(CalcTypes type) 
	{
		this.type = type;
		switch (this.type)
		{
			case FCT:
				assignment = new HashMap<String, String>();
				this.parameters = new Calculation[0];
				break;
			case IFE: 
				this.parameters = new Calculation[3];
				break;
			case IF:
			case ADD:
			case SUB:
			case MUL:
			case DIV:
			case POW:
			case ROOT:
			case MIN:
			case MAX:
			case AND:
			case OR:
			case XOR:
			case BIGR:
			case SMLR:
			case EBIG:
			case ESML:
			case EQU:
			case MOD:
				this.parameters = new Calculation[2];
				break;
			case SQR:
			case SQROOT:
			case SUM:
			case PRCT:
			case NOT:
			case INTGR:
			case FAC:
				this.parameters = new Calculation[1];
				break;
			case DIGIT:
			case VAR:
				this.parameters = new Calculation[0];
				break;
		}
		value = 0.0;
		variable = "";
	}

	public double calculate(JsonObject variables) throws Exception
	{
		double result = 0.0;

		switch (this.type)
		{
			case FAC:
				int temp = (int)this.parameters[0].calculate(variables);
				if (temp < 0) result = Double.NaN;
				else
				{
					result = 1.0;
					while (temp > 0) 
					{
						result *= temp;
						temp--;
					}
				}
				break;
			case ADD:
				result = this.parameters[0].calculate(variables) + this.parameters[1].calculate(variables);
				break;
			case SUB:
				result = this.parameters[0].calculate(variables) - this.parameters[1].calculate(variables);
				break;
			case MUL:
				result = this.parameters[0].calculate(variables) * this.parameters[1].calculate(variables);
				break;
			case DIV:
				if (this.parameters[1].calculate(variables) == 0.0) result = Double.NaN;
				else result = this.parameters[0].calculate(variables) / this.parameters[1].calculate(variables);
				break;
			case SQR:
				result = this.parameters[0].calculate(variables) * this.parameters[0].calculate(variables);
				break;
			case POW:
				result = Math.pow(this.parameters[0].calculate(variables), this.parameters[1].calculate(variables));
				break;
			case SQROOT:
				result = Math.sqrt(this.parameters[0].calculate(variables));
				break;
			case ROOT:
				if (this.parameters[1].calculate(variables) == 0.0) result = Double.NaN;
				else result = Math.pow(this.parameters[0].calculate(variables), 1.0 / this.parameters[1].calculate(variables));
				break;
			case MIN:
				result = Math.min(this.parameters[0].calculate(variables), this.parameters[1].calculate(variables));
				break;
			case MAX:
				result = Math.max(this.parameters[0].calculate(variables), this.parameters[1].calculate(variables));
				break;
			case AND:
				double resA1 = this.parameters[0].calculate(variables);
				double resA2 = this.parameters[1].calculate(variables);
				if (resA1 != 0 && resA2 != 0) result = 1;
				else result = 0;
				break;
			case XOR:
				double resX1 = this.parameters[0].calculate(variables);
				double resX2 = this.parameters[1].calculate(variables);
				if ((resX1 != 0 && resX2 == 0) || (resX1 == 0 && resX2 != 0)) result = 1;
				else result = 0;
				break;
			case OR:
				double resO1 = this.parameters[0].calculate(variables);
				double resO2 = this.parameters[1].calculate(variables);
				if (resO1 != 0 || resO2 != 0) result = 1;
				else result = 0;
				break;
			case NOT:
				double resN = this.parameters[0].calculate(variables);
				if (resN == 0) result = 1;
				else result = 0;
				break;
			case IFE:
				double resIE = this.parameters[0].calculate(variables);
				if (resIE != 0) result = this.parameters[1].calculate(variables);
				else result = this.parameters[2].calculate(variables);
				break;
			case IF:
				double resIf = this.parameters[0].calculate(variables);
				if (resIf != 0) result = this.parameters[1].calculate(variables);
				else result = 0.0;
				break;
			case BIGR:
				if (this.parameters[0].calculate(variables) > this.parameters[1].calculate(variables)) result = 1;
				else result = 0;
				break;
			case SMLR:
				if (this.parameters[0].calculate(variables) < this.parameters[1].calculate(variables)) result = 1;
				else result = 0;
				break;
			case EBIG:
				if (this.parameters[0].calculate(variables) >= this.parameters[1].calculate(variables)) result = 1;
				else result = 0;
				break;
			case ESML:
				if (this.parameters[0].calculate(variables) <= this.parameters[1].calculate(variables)) result = 1;
				else result = 0;
				break;
			case EQU:
				if (this.parameters[0].calculate(variables) == this.parameters[1].calculate(variables)) result = 1;
				else result = 0;
				break;
			case MOD:
				if (this.parameters[1].calculate(variables) > 0) 
				{
					result = this.parameters[0].calculate(variables) % this.parameters[1].calculate(variables);
				}
				else result = Double.NaN;
				break;
			case DIGIT:
				result = this.value;
				break;	
			case VAR:
				if (this.variable.equals("e") && !variables.has("e")) result = Math.E;
				else result = StaticTools.JsonGetDouble(variables, this.variable);
				break;
			case SUM:
				int[] indizes = StaticTools.jsonGetArrayInt(variables, this.variable);
				
				if (indizes.length != 2) result = 0;
				else
				{
					List<String> iVars = StaticTools.getVarsWithIndex(variables, this.variable);
					Map<String, JsonArray> iValues = StaticTools.jsonGetJsonArrayMap(variables, iVars);
					result = 0.0;
					
					String varTemp = variables.toString();
					JsonObject varCopy = new Gson().fromJson(varTemp, JsonObject.class);
					
					for (int i = indizes[0]; i <= indizes[1]; i++)
					{
						varCopy.remove(this.variable);
						varCopy.addProperty(this.variable, i);
						Iterator<String> it = iVars.iterator();
						while (it.hasNext()) 
						{
							String var = it.next();
							varCopy.remove(var);
							varCopy.add(var, iValues.get(var).get(i));
						}
						result += this.parameters[0].calculate(varCopy);
					}
				}
				break;
			case PRCT:
				int[] indizesP = StaticTools.jsonGetArrayInt(variables, this.variable);
				
				if (indizesP.length != 2) result = 0;
				else
				{
					List<String> iVars = StaticTools.getVarsWithIndex(variables, this.variable);
					Map<String, JsonArray> iValues = StaticTools.jsonGetJsonArrayMap(variables, iVars);
					result = 1.0;
					
					String varTemp = variables.toString();
					JsonObject varCopy = new Gson().fromJson(varTemp, JsonObject.class);
					
					for (int i = indizesP[0]; i <= indizesP[1]; i++)
					{
						varCopy.remove(this.variable);
						varCopy.addProperty(this.variable, i);
						Iterator<String> it = iVars.iterator();
						while (it.hasNext()) 
						{
							String var = it.next();
							varCopy.remove(var);
							varCopy.add(var, iValues.get(var).get(i));
						}
						result *= this.parameters[0].calculate(varCopy);
					}
				}
				break;
			case INTGR:
				int[] izsIgr = StaticTools.jsonGetArrayInt(variables, this.variable);
				
				if (izsIgr.length != 2) result = 0;
				else
				{
					JsonArray jAr = StaticTools.jsonGetJsonArray(variables, this.variable);
					result = 0.0;
					if (jAr.size() == 2)
					{
						double a = jAr.get(0).getAsDouble();
						double b = jAr.get(1).getAsDouble();
						int n = 100;
						
						if (a <= b)
						{
							double h = (b-a) / n;
							double tmp = 0.0;
							
							String varTemp = variables.toString();
							JsonObject varCopy = new Gson().fromJson(varTemp, JsonObject.class);
							
							for (int i = 1; i <= n; i++)
							{
								double x = a - (h/2) + (i*h);
								
								varCopy.remove(this.variable);
								varCopy.addProperty(this.variable, x);
								tmp += this.parameters[0].calculate(varCopy);
							}
							result = h * tmp;
						}
					}
				}
				break;
			case FCT:	
				Iterator<String> itR = this.assignment.keySet().iterator();
				
				String jsonStr = "";
				while (itR.hasNext())
				{
					if (jsonStr.equals("")) jsonStr += "{ ";
					else jsonStr += ", ";
					String k = itR.next();
					String v = assignment.get(k); 
					if (!StaticTools.isDigit(v))
					{
						JsonElement jsonVal = variables.get(v);
						v = jsonVal.toString();
					}
					jsonStr += "\"" + k + "\": " + v;
				}
				if (jsonStr.equals("")) jsonStr = "{ }";
				else jsonStr += " }";
				
				Gson gson = new Gson();
				JsonElement element = gson.fromJson (jsonStr, JsonElement.class);
				JsonObject jsonObj = element.getAsJsonObject();
				
				if (this.remoteObject != null) result = this.remoteObject.calculate(jsonObj);
				else result = Double.NaN;
				
				break;
			default:
				result = 0.0;
				break;
		}
		return result;
	}
	
	public final void setRemoteFunction(String fct) throws Exception
	{
		this.remoteFct = fct;
		RESTFunction rst = new RESTFunction();
		this.remoteObject = rst.getCalculation(this.remoteFct);
	}
	
	public final void setAssignment(String assignment)
	{
		StringTokenizer tokens = new StringTokenizer(assignment, "$");
		while (tokens.hasMoreTokens())
		{
			String token = tokens.nextToken();
			StringTokenizer ass = new StringTokenizer(token, ":");
			this.assignment.put(ass.nextToken(), ass.nextToken());
		}
	}
	
	public final void setParameter(Calculation parm, int pos)
	{
		if (this.parameters.length <= pos) return;
		this.parameters[pos] = parm;
	}
	
	public final void setParameter(Calculation parm1, Calculation parm2)
	{
		if (this.parameters.length != 2) return;
		this.parameters[0] = parm1;
		this.parameters[1] = parm2;
	}
	
	public final void setParameter(Calculation parm1)
	{
		if (this.parameters.length != 1) return;
		this.parameters[0] = parm1;
	}
	
	public final void setMidpointN(int n)
	{
		this.midpointN = n;	
	}
	
	/**
	 * @return the type
	 */
	public final CalcTypes getType() {
		return type;
	}
	
	/**
	 * @return the parameters
	 */
	public final Calculation[] getParamters() {
		return parameters;
	}

	/**
	 * @return the value
	 */
	public final double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public final void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the variable
	 */
	public final String getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	public final void setVariable(String variable) {
		this.variable = variable;
	}
}