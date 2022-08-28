JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=typingTutor/typingTutor/src/typingTutor
BINDIR=typingTutor/typingTutor/bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES=Score.class WordDictionary.class BackgroundSound.class FallingWord.class WordMover.class HungryWordMover.class DuplicateRemover.class CatchWord.class ScoreUpdater.class GamePanel.class TypingTutorApp.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm -f $(BINDIR)/typingTutor/*.class

run-Game: $(CLASS_FILES)
	@java -cp typingTutor/typingTutor/bin typingTutor.TypingTutorApp $(ARGS)

javaDoc:
	javadoc -d doc typingTutor/typingTutor/src/typingTutor/*.java