package ayds.winchester.songinfo.moredetails.presentation

import java.util.*

interface DescriptionFormatter {
    fun format(snippet: String, artistName: String): String
}

private const val HTML_START_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END = "</font></div></html>"

internal class HtmlDescriptionFormatter: DescriptionFormatter{

    override fun format(snippet: String, artistName: String): String {
        val text1 = snippet.replace("\\n", "\n")
        return textToHtml(text1, artistName)
    }

    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append(HTML_START_WIDTH)
        builder.append(HTML_FONT)
        val textWithBold = formatTextWithBold(text, term)
        builder.append(textWithBold)
        builder.append(HTML_END)
        return builder.toString()
    }

    private fun formatTextWithBold(text: String, term: String): String {
        return text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
            )
    }

}