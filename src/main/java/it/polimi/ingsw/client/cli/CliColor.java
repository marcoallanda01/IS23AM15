package it.polimi.ingsw.client.cli;

/**
 * This class contains all the ANSI escape codes for colors and styles.
 * It is used to color the CLI output.
 */
public class CliColor {
    // Reset
    /**
     * This ANSI escape code resets the color and style of the text.
     */
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    /**
     * This ANSI escape code sets the color of the text to black.
     */
    public static final String BLACK = "\033[0;30m";   // BLACK
    /**
     * This ANSI escape code sets the color of the text to red.
     */
    public static final String RED = "\033[0;31m";     // RED
    /**
     * This ANSI escape code sets the color of the text to green.
     */
    public static final String GREEN = "\033[0;32m";   // GREEN
    /**
     * This ANSI escape code sets the color of the text to yellow.
     */
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    /**
     * This ANSI escape code sets the color of the text to blue.
     */
    public static final String BLUE = "\033[0;34m";    // BLUE
    /**
     * This ANSI escape code sets the color of the text to purple.
     */
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    /**
     * This ANSI escape code sets the color of the text to cyan.
     */
    public static final String CYAN = "\033[0;36m";    // CYAN
    /**
     * This ANSI escape code sets the color of the text to white.
     */
    public static final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    /**
     * This ANSI escape code sets the style of the text to bold and the color to black.
     */
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    /**
     * This ANSI escape code sets the style of the text to bold and the color to red.
     */
    public static final String RED_BOLD = "\033[1;31m";    // RED
    /**
     * This ANSI escape code sets the style of the text to bold and the color to green.
     */
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    /**
     * This ANSI escape code sets the style of the text to bold and the color to yellow.
     */
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    /**
     * This ANSI escape code sets the style of the text to bold and the color to blue.
     */
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    /**
     * This ANSI escape code sets the style of the text to bold and the color to purple.
     */
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    /**
     * This ANSI escape code sets the style of the text to bold and the color to cyan.
     */
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    /**
     * This ANSI escape code sets the style of the text to bold and the color to white.
     */
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    // Underline
    /**
     * This ANSI escape code sets the style of the text to underlined and the color to black.
     */
    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    /**
     * This ANSI escape code sets the style of the text to underlined and the color to red.
     */
    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
    /**
     * This ANSI escape code sets the style of the text to underlined and the color to green.
     */
    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    /**
     * This ANSI escape code sets the style of the text to underlined and the color to yellow.
     */
    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    /**
     * This ANSI escape code sets the style of the text to underlined and the color to blue.
     */
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    /**
     * This ANSI escape code sets the style of the text to underlined and the color to purple.
     */
    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    /**
     * This ANSI escape code sets the style of the text to underlined and the color to cyan.
     */
    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    /**
     * This ANSI escape code sets the style of the text to underlined and the color to white.
     */
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE
    /**
     * This ANSI escape code sets the style of the text to underlined and the color to white and bright.
     */
    public static final String WHITE_UNDERLINED_BRIGHT = "\033[4;97m";  // WHITE

    // Background
    /**
     * This ANSI escape code sets the background color of the text to black.
     */
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    /**
     * This ANSI escape code sets the background color of the text to red.
     */
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    /**
     * This ANSI escape code sets the background color of the text to green.
     */
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    /**
     * This ANSI escape code sets the background color of the text to yellow.
     */
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    /**
     * This ANSI escape code sets the background color of the text to blue.
     */
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    /**
     * This ANSI escape code sets the background color of the text to purple.
     */
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    /**
     * This ANSI escape code sets the background color of the text to cyan.
     */
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    /**
     * This ANSI escape code sets the background color of the text to white.
     */
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    // High Intensity
    /**
     * This ANSI escape code sets the color of the text to black and bright.
     */
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    /**
     * This ANSI escape code sets the color of the text to red and bright.
     */
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    /**
     * This ANSI escape code sets the color of the text to green and bright.
     */
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    /**
     * This ANSI escape code sets the color of the text to yellow and bright.
     */
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    /**
     * This ANSI escape code sets the color of the text to blue and bright.
     */
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    /**
     * This ANSI escape code sets the color of the text to purple and bright.
     */
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    /**
     * This ANSI escape code sets the color of the text to cyan and bright.
     */
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    /**
     * This ANSI escape code sets the color of the text to white and bright.
     */
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    /**
     * This ANSI escape code sets the style of the text to bold and the color to black and bright.
     */
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    /**
     * This ANSI escape code sets the style of the text to bold and the color to red and bright.
     */
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    /**
     * This ANSI escape code sets the style of the text to bold and the color to green and bright.
     */
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    /**
     * This ANSI escape code sets the style of the text to bold and the color to yellow and bright.
     */
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    /**
     * This ANSI escape code sets the style of the text to bold and the color to blue and bright.
     */
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    /**
     * This ANSI escape code sets the style of the text to bold and the color to purple and bright.
     */
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    /**
     * This ANSI escape code sets the style of the text to bold and the color to cyan and bright.
     */
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    /**
     * This ANSI escape code sets the style of the text to bold and the color to white and bright.
     */
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    /**
     * This ANSI escape code sets the background color of the text to black and bright.
     */
    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    /**
     * This ANSI escape code sets the background color of the text to red and bright.
     */
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    /**
     * This ANSI escape code sets the background color of the text to green and bright.
     */
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    /**
     * This ANSI escape code sets the background color of the text to yellow and bright.
     */
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    /**
     * This ANSI escape code sets the background color of the text to blue and bright.
     */
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    /**
     * This ANSI escape code sets the background color of the text to purple and bright.
     */
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    /**
     * This ANSI escape code sets the background color of the text to cyan and bright.
     */
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    /**
     * This ANSI escape code sets the background color of the text to white and bright.
     */
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE

}
