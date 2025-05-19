package ed.lab;
import java.util.*;

class E02AutocompleteSystem {

    private class TrieNode {
        Map<Character, TrieNode> children;
        Map<String, Integer> counts;

        TrieNode() {
            children = new HashMap<>();
            counts = new HashMap<>();
        }
    }

    private TrieNode root;
    private StringBuilder currentInput;
    private TrieNode currentNode;

    public E02AutocompleteSystem(String[] sentences, int[] times) {
        root = new TrieNode();
        currentInput = new StringBuilder();
        currentNode = root;

        for (int i = 0; i < sentences.length; i++) {
            insert(sentences[i], times[i]);
        }
    }

    private void insert(String sentence, int count) {
        TrieNode node = root;
        for (char c : sentence.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
            node.counts.put(sentence, node.counts.getOrDefault(sentence, 0) + count);
        }
    }

    public List<String> input(char c) {
        if (c == '#') {
            String inputSentence = currentInput.toString();
            insert(inputSentence, 1);
            currentInput = new StringBuilder();
            currentNode = root;
            return new ArrayList<>();
        }

        currentInput.append(c);

        if (currentNode != null) {
            currentNode = currentNode.children.get(c);
        }

        if (currentNode == null) {
            return new ArrayList<>();
        }

        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((a, b) -> {
            if (!a.getValue().equals(b.getValue())) {
                return b.getValue() - a.getValue(); // Higher frequency first
            }
            return a.getKey().compareTo(b.getKey()); // Lexicographical order
        });

        pq.addAll(currentNode.counts.entrySet());

        List<String> result = new ArrayList<>();
        int k = 3;
        while (!pq.isEmpty() && k-- > 0) {
            result.add(pq.poll().getKey());
        }

        return result;
    }
}