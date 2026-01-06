package com.mockinterview.mockinterview;

import java.util.Arrays;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.TokenizerME;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TagGenerator {


  private static TagGenerator instance;

  private final TokenizerME tokenizer;
  private final POSTaggerME posTagger;

  public TagGenerator() throws Exception {
    try (InputStream tokenModelIn = getClass().getResourceAsStream(
        "/models/opennlp-en-ud-ewt-tokens.bin");
        InputStream posModelIn = getClass().getResourceAsStream(
            "/models/opennlp-en-ud-ewt-pos.bin")) {

      TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
      tokenizer = new TokenizerME(tokenModel);

      POSModel posModel = new POSModel(posModelIn);
      posTagger = new POSTaggerME(posModel);
    }
  }

  // Public accessor
  public static synchronized TagGenerator getInstance() throws Exception {
    if (instance == null) {
      instance = new TagGenerator();
    }
    return instance;
  }

  public String[] extractNouns(String sentence) {
    String[] tokens = tokenizer.tokenize(sentence);
    String[] posTags = posTagger.tag(tokens);

    String[] nouns = new String[tokens.length];
    int k = 0;
    for (int i = 0; i < tokens.length; i++) {
      if (posTags[i].startsWith("NOUN")) { // NN = noun
        nouns[k] = tokens[i];
        k++;
      }
    }
    return Arrays.copyOf(nouns, k);
  }
}

