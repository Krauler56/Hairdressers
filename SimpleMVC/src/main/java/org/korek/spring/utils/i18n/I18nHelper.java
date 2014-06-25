package org.korek.spring.utils.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

public class I18nHelper
{

	private static final String ENCODING = "ISO-8859-1";
	private static final String PATTERN_STRING = "#\\{(.*?)\\}";
	private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

	private static final File BASE_HTML_DIR = new File("src/main/webapp/WEB-INF/views");
	private static final String[] EXTENTIONS = { "html" };

	private static final String BASE_TRANS_DIR = "src/main/resources/i18n";

	private static final File ALL = new File(BASE_TRANS_DIR + "/generated/all");

	private static final File ALL_TRANS_DIR = new File(BASE_TRANS_DIR);
	private static final String[] TRANS_EXTENSIONS = { "properties" };;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException
	{

		Set<String> allWords = findAllTransInHTMLFiles();
		System.out.println("Total words = " + allWords.size());

		Collection<File> allTransFiles = FileUtils.listFiles(ALL_TRANS_DIR, TRANS_EXTENSIONS, false);

		findMissingTrans(allTransFiles, allWords);

		sortTransFiles(allTransFiles);

	}

	@SuppressWarnings("unchecked")
	private static Set<String> findAllTransInHTMLFiles() throws IOException
	{
		Collection<File> allHTMLFiles = FileUtils.listFiles(BASE_HTML_DIR, EXTENTIONS, true);

		Set<String> allWords = new TreeSet<>();

		for (File file : allHTMLFiles)
		{
			String fileContent = FileUtils.readFileToString(file, ENCODING);

			Matcher matcher = PATTERN.matcher(fileContent);

			while (matcher.find())
			{
				allWords.add(matcher.group(1) + "=");
			}
		}

		FileUtils.writeLines(ALL, ENCODING, allWords);

		return allWords;
	}

	@SuppressWarnings("unchecked")
	private static void findMissingTrans(Collection<File> allTransFiles, Set<String> allWords) throws IOException
	{
		for (File transFile : allTransFiles)
		{
			List<String> lines = FileUtils.readLines(transFile, ENCODING);
		
			List<String> translatedWords = Lists.newArrayListWithCapacity(lines.size());

			for (String line : lines)
			{
				if (!line.isEmpty())
				{
					int end = line.indexOf("=");
					if (end != -1)
					{
						line = line.substring(0, end + 1);
						translatedWords.add(line);
					}
				}
			}

			
			List<String> missing = Lists.newArrayList();
			for (String word : allWords)
			{
				if (!translatedWords.contains(word))
				{
					missing.add(word);
				}
			}

			String transFileName = transFile.getName();
			System.out.println(missing.size() + " translations missing from " + transFileName);
			File missingFile = new File(BASE_TRANS_DIR + "/generated/" + transFileName + "_missing");
			FileUtils.writeLines(missingFile, ENCODING ,missing);
		}

	}

	private static void sortTransFiles(Collection<File> allTransFiles) throws IOException
	{
		for (File transFile : allTransFiles)
		{
			List<String> linkedLines = readLines(transFile); // faster remove operation on linked list - however in this case will be slower anyway because of size (~150 elements so far)
			Collections.sort(linkedLines);

			Character prevFirst = null;
			for (ListIterator<String> listIterator = linkedLines.listIterator(); listIterator.hasNext();)
			{
				String string = (String) listIterator.next();
				if (string.isEmpty())
				{
					listIterator.remove();
				}
				else
				{
					Character firstLetter = string.charAt(0);
					if (prevFirst != firstLetter)
					{
						if (listIterator.hasPrevious())
							listIterator.previous();
						listIterator.add("");
						if (listIterator.hasNext())
							listIterator.next();
						prevFirst = firstLetter;
					}
				}
			}

			FileUtils.writeLines(transFile, ENCODING, linkedLines);
		}
	}

	private static List<String> readLines(File file) throws IOException
	{
		List<String> linkedLines = Lists.newLinkedList();

		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING));
			
			for (String line; (line = br.readLine()) != null;)
			{
				linkedLines.add(line);
			}
		}
		finally
		{
			if (br != null)
			{
				br.close();
			}
		}

		return linkedLines;
	}

}
