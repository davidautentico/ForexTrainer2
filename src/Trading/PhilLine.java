package Trading;

import utils.PrintUtils;

public class PhilLine {

	LineType lineType;
	double value;
			
	public LineType getLineType() {
		return lineType;
	}

	public void setLineType(LineType lineType) {
		this.lineType = lineType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}


        public String toString(){
		return this.lineType.name()+":"+PrintUtils.Print(this.value);
	}

	public static PhilLine createLine(LineType lineType,double value){
		PhilLine line = new PhilLine();
		line.setLineType(lineType);
		line.setValue(value);
		
		return line;
	}

}
