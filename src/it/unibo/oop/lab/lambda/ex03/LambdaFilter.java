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
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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

    private static List<String> convertToStringStream(String s, final List<Character> separator) {
        List<String> list = new ArrayList<>();
        String word = "";
        s = s.concat(Character.toString(LINE_SEPARATOR));

        for (Character c : s.toCharArray()) {
            if (!separator.contains(c)) {
                word = word.concat(Character.toString(c));
            } else {
                list.add(word);
                word = "";
            }
        }
        return list;
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

        COUNT("Count", s -> Integer.toString(convertToStringStream(s, List.of(LINE_SEPARATOR, WORD_SEPARATOR)).size())),

        ORDER_ALPHABETICAL("Ordered alphabetical", s -> {
            String r = "";
            List<String> l = convertToStringStream(s, List.of(LINE_SEPARATOR, WORD_SEPARATOR));
            Collections.sort(l);
            for (String itm : l) {
                r += itm + LINE_SEPARATOR;
            }
            return r;
        }),

        COUNTING_WORD("Counting word", s -> {
            String r = "";
            Map<String, Integer> mappa = new HashMap<>();
            List<String> l = convertToStringStream(s, List.of(LINE_SEPARATOR, WORD_SEPARATOR));
            Collections.sort(l);

            for (String c : l) {
                if (mappa.keySet().contains(c)) {
                    int v = mappa.get(c);
                    v++;
                    mappa.put(c, v);
                } else {
                    mappa.put(c, 1);
                }
            }

            for (var v : mappa.entrySet()) {
                r = r.concat(v.getKey() + " -> " + v.getValue() + LINE_SEPARATOR);
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
