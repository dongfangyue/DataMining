package Gump.DataMing;

public class ResultOutput {

	public static void main(String[] args) throws Exception {
		ResultOutput output = new ResultOutput();
		output.getFeature();
	}

	private void getFeature() throws Exception {
		// TODO Auto-generated method stub
		FeatureSelection feature = new FeatureSelection();
		feature.feature_select();
	}
}