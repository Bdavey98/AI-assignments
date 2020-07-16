
/*  Author: Brenden Davey
    Course: CIS 421 Artificial Intelligence
    Assignment: 6
*/

//This class creates the neurons in each layer
public class Neuron {

    double[] weights;
    double[] updatedWeights;
    double change;
    double value = 0;

    // constructor for the input neurons
    public Neuron(double value) {
        this.updatedWeights = this.weights;
        this.change = -1;
        this.value = value;
    }

    // constructor for the hidden / output neurons
    // (difference is the weights and input value)
    public Neuron(double[] weights) {
        this.weights = weights;
        this.updatedWeights = this.weights;
        this.change = 0;
    }

    // updates the weights after the back propagation
    public void update_weight() {
        this.weights = this.updatedWeights;
    }
}