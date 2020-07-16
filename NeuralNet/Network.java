
/*  Author: Brenden Davey
    Course: CIS 421 Artificial Intelligence
    Assignment: 6
*/

// this class creates the network layers
public class Network {
	public Neuron[] neurons;

	// constructor for the hidden and output Layers
	public Network(int inNeurons, int numberNeurons) {
		this.neurons = new Neuron[numberNeurons];

		for (int i = 0; i < numberNeurons; i++) {
			double[] weights = new double[inNeurons];
			for (int j = 0; j < inNeurons; j++) {
				weights[j] = randomDouble(-.5, .5);
			}
			neurons[i] = new Neuron(weights);
		}
	}

	// constructor for the input Layer
	public Network(double input[]) {
		this.neurons = new Neuron[input.length];
		for (int i = 0; i < input.length; i++) {
			this.neurons[i] = new Neuron(input[i]);
		}
	}

	// returns a random double between -.5 and .5
	public double randomDouble(double min, double max) {
		double a = (double) Math.random();
		double num = min + (double) Math.random() * (max - min);
		if (a < 0.5)
			return num;
		else
			return -num;
	}

}