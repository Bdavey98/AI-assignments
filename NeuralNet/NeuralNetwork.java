
/*  Author: Brenden Davey
    Course: CIS 421 Artificial Intelligence
    Assignment: 6
*/

public class NeuralNetwork {

    static Network[] layers;

    // XOR training data
    static double[][] inputs = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
    static double[] expectedOutputs = { 0, 1, 1, 0 };

    static int index;
    static int pass = 0;

    // This program implements a simple multi-layer neural network to learn
    // logical XOR with two input nodes, a hidden layer, and a single output node.
    public static void main(String[] args) {

        initialize();

        // Prints the output before any training
        System.out.println("Output before training");
        for (int i = 0; i < 4; i++) {
            feedForward(inputs[i]);
            System.out.println(layers[2].neurons[0].value);
            System.out.println("Expected Value: " + expectedOutputs[i]);
            System.out.println();
        }

        train(5000, 0.5);

        // Prints the output after training along with the expected output
        System.out.println();
        System.out.println("Output after training");
        System.out.println();
        for (int i = 0; i < 4; i++) {
            feedForward(inputs[i]);
            System.out.println(layers[2].neurons[0].value);
            System.out.println("Expected Value: " + expectedOutputs[i]);
            System.out.println();
            if (layers[2].neurons[0].value > .9)
                pass++;
        }
        System.out.println();
        // ff the values are >.9, the training passed
        if (pass > 1)
            System.out.println("Training Passed");
        else
            System.out.println("Training Failed");
    }

    // function that initialized the layers
    // post condition: layer is initialized with the neurons
    public static void initialize() {
        // create the layers with neurons
        layers = new Network[3];
        // input Layer
        layers[0] = null;
        // hidden layer with 3 neurons
        layers[1] = new Network(2, 3);
        // output Layer
        layers[2] = new Network(3, 1);

    }

    // function trains network through iterations with backpropogation
    // param: int iterations - number of training iterations to run
    // param: double learningRate - the learning rate
    // pre-condition: initialize has been run
    // post-condition: network will be trained
    public static void train(int iterations, double learningRate) {
        for (int i = 0; i < iterations; i++) {

            if (i % 100 == 0) {
                System.out.println();
                System.out.println("Output during training");

                for (int j = 0; j < 4; j++) {
                    feedForward(inputs[j]);
                    System.out.println(layers[2].neurons[0].value);
                }
            }

            for (int j = 0; j < 4; j++) {
                feedForward(inputs[j]);
                backPropagation(learningRate);
            }
        }
    }

    // param: (double []) inputs - XOR input data
    // pre-condition: initialize has been run
    // post-condition: inputs provided to network
    public static void feedForward(double[] inputs) {
        layers[0] = new Network(inputs);
        for (int i = 1; i < layers.length; i++) {
            for (int j = 0; j < layers[i].neurons.length; j++) {
                double sum = 0;
                for (int k = 0; k < layers[i - 1].neurons.length; k++) {
                    sum += layers[i - 1].neurons[k].value * layers[i].neurons[j].weights[k];
                }
                // computeActivation
                layers[i].neurons[j].value = (1 / (1 + Math.pow(Math.E, -sum)));
            }
        }
    }

    // param: double learningRate - value between 0 and 1
    // pre-condition: feedforward has been run
    // post-condition: weights are updated
    public static void backPropagation(double learningRate) {
        int number_layers = layers.length;
        int out_index = number_layers - 1;
        // computeError for the output layer
        for (int i = 0; i < layers[out_index].neurons.length; i++) {
            double output = layers[out_index].neurons[i].value;
            double target = expectedOutputs[index % 4];
            double delta = (output - target) * (output * (1 - output));
            layers[out_index].neurons[i].change = delta;
            // computerWeightChange for the output layer
            for (int j = 0; j < layers[out_index].neurons[i].weights.length; j++) {
                double previous_output = layers[out_index - 1].neurons[j].value;
                double error = delta * previous_output;
                layers[out_index].neurons[i].updatedWeights[j] = layers[out_index].neurons[i].weights[j]
                        - learningRate * error;
            }
            index++;
        }

        // computeError for hidden layer
        for (int i = out_index - 1; i > 0; i--) {
            for (int j = 0; j < layers[i].neurons.length; j++) {
                double output = layers[i].neurons[j].value;
                double target = 0;
                Network current_layer = layers[i + 1];
                for (int k = 0; k < current_layer.neurons.length; k++) {
                    Neuron current_neuron = current_layer.neurons[k];
                    target += current_neuron.weights[j] * current_neuron.change;
                }
                double delta = target * (output * (1 - output));
                layers[i].neurons[j].change = delta;
                // computeWeightChange for the hidden layer
                for (int k = 0; k < layers[i].neurons[j].weights.length; k++) {
                    double previous_output = layers[i - 1].neurons[k].value;
                    double error = delta * previous_output;
                    layers[i].neurons[j].updatedWeights[k] = layers[i].neurons[j].weights[k] - learningRate * error;
                }
            }
        }

        // Update all the weights
        for (int i = 0; i < layers.length; i++) {
            for (int j = 0; j < layers[i].neurons.length; j++) {
                layers[i].neurons[j].update_weight();
            }
        }
    }
}