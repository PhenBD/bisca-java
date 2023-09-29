package Graphics;
import java.util.Random;

public class QLearning {
    private double[][] Q; // Tabela Q que armazena os valores Q para pares estado-ação
    private final int numStates;
    private final int numActions;
    private final double learningRate;
    private final double discountFactor;

    public QLearning(int numStates, int numActions, double learningRate, double discountFactor) {
        this.numStates = numStates;
        this.numActions = numActions;
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;

        Q = new double[numStates][numActions];
        initializeQTable();
    }

    private void initializeQTable() {
        Random random = new Random();
        for (int state = 0; state < numStates; state++) {
            for (int action = 0; action < numActions; action++) {
                Q[state][action] = random.nextDouble();
            }
        }
    }

    public int selectAction(int state) {
        Random random = new Random();
        if (random.nextDouble() < 0.3) {
            // Explore: Escolha uma ação aleatória com probabilidade 0.3
            return random.nextInt(numActions);
        } else {
            // Exploite: Escolha a ação com o maior valor Q
            int bestAction = 0;
            for (int action = 1; action < numActions; action++) {
                if (Q[state][action] > Q[state][bestAction]) {
                    bestAction = action;
                }
            }
            return bestAction;
        }
    }

    public void updateQValue(int state, int action, double reward, int nextState) {
        // Atualize o valor Q com base na recompensa e no próximo estado
        double maxQNextState = getMaxQValue(nextState);
        Q[state][action] = (1 - learningRate) * Q[state][action] +
                          learningRate * (reward + discountFactor * maxQNextState);
    }

    private double getMaxQValue(int state) {
        // Encontre o maior valor Q para um estado dado
        double maxQ = Q[state][0];
        for (int action = 1; action < numActions; action++) {
            if (Q[state][action] > maxQ) {
                maxQ = Q[state][action];
            }
        }
        return maxQ;
    }
}