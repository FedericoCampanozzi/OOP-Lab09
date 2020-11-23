package it.unibo.oop.lab.lambda.ex03;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import static java.util.stream.Collectors.toList;

/**
 * Modify this small program adding new filters.
 * Realize this exercise using as much as possible the Stream library.
 * 
 * 1) Convert to lowercase
 * 
 * 2) Count the number of chars
 * 
 * 3) Count the number of lines
 * 
 * 4) List all the words in alphabetical order
 * 
 * 5) Write the count for each word, e.g. "word word pippo" should output "pippo -> 1 word -> 2"
 *
 */
public final class LambdaFilter extends JFrame {

    private static final long serialVersionUID = 1760990730218643730L;
    private static final char LINE_SEPARATOR = '\n';
    private static final char WORD_SEPARATOR = ' ';

    private static Stream<String> convertToStringStream(final String s, final List<Character> separator) {
        List<String> list = new ArrayList<>();
        String word = "";
        for (Character c : s.toCharArray()) {
            if (separator.contains(c)) {
                word += c.toString();
            } else {
                list.add(word);
                word = "";
            }
        }
        return list.stream();
    }

    private static Stream<Character> convertToCharStream(final String s, final List<Character> ignore) {
        List<Character> list = new ArrayList<>();
        for (Character c : s.toCharArray()) {
            if (!ignore.contains(c)) {
                list.add(c);
            }
        }
        return list.stream();
    }

    private enum Command {
        IDENTITY("No modifications", Function.identity()),
        TO_LOWER("To lower", s -> {
            String lower = "";
            for (Character c : s.toCharArray()) {
                lower += Character.toLowerCase(c);
            }
            return lower;
        }),

        COUNT("Count", s -> Integer.toString((int) convertToCharStream(s, List.of(LINE_SEPARATOR)).count())),

        COUNT_LINE("Count line", s -> Integer.toString((int) convertToStringStream(s, List.of(LINE_SEPARATOR)).count())),

        ORDER_ALPHABETICAL("Ordered alphabetical", s -> {
            String r = "";
            for (String c : convertToStringStream(s, List.of(LINE_SEPARATOR, WORD_SEPARATOR)).sorted(String::compareTo).collect(toList())) {
                r += c + LINE_SEPARATOR;
            }
            return r;
        }),

        COUNTING_WORD("Counting word", s -> {
            String r = "";
            Map<String, Integer> mappa = new HashMap<>();
            convertToStringStream(s, List.of(LINE_SEPARATOR, WORD_SEPARATOR)).forEach(d -> System.out.println(d + "."));
            for (String c : convertToStringStream(s, List.of(LINE_SEPARATOR, WORD_SEPARATOR)).sorted(String::compareTo).collect(toList())) {
                if (mappa.keySet().contains(c)) {
                    int v = mappa.get(c);
                    v++;
                    mappa.put(c, v);
                } else {
                    mappa.put(c, 1);
                }
            }

            for (var v : mappa.entrySet()) {
                r = r.concat(v.getKey() + " -> " + v.getValue());
            }
            return r;
        });

        private final String commandName;
        private final Function<String, String> fun;

        Command(final String name, final Function<String, String> process) {
            commandName = name;
            fun = process;
        }

        @Override
        public String toString() {
            return commandName;
        }

        public String translate(final String s) {
            return fun.apply(s);
        }
    }

    private LambdaFilter() {
        super("Lambda filter GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panel1 = new JPanel();
        final LayoutManager layout = new BorderLayout();
        panel1.setLayout(layout);
        final JComboBox<Command> combo = new JComboBox<>(Command.values());
        panel1.add(combo, BorderLayout.NORTH);
        final JPanel centralPanel = new JPanel(new GridLayout(1, 2));
        final JTextArea left = new JTextArea();
        left.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        final JTextArea right = new JTextArea();
        right.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        right.setEditable(false);
        centralPanel.add(left);
        centralPanel.add(right);
        panel1.add(centralPanel, BorderLayout.CENTER);
        final JButton apply = new JButton("Apply");
        apply.addActionListener(ev -> right.setText(((Command) combo.getSelectedItem()).translate(left.getText())));
        panel1.add(apply, BorderLayout.SOUTH);
        setContentPane(panel1);
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int sw = (int) screen.getWidth();
        final int sh = (int) screen.getHeight();
        setSize(sw / 4, sh / 4);
        setLocationByPlatform(true);
    }

    /**
     * @param a unused
     */
    public static void main(final String... a) {
        final LambdaFilter gui = new LambdaFilter();
        gui.setVisible(true);
    }
}
