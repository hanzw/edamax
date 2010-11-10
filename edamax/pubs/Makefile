TARGET=foga09-eda
LATEX=pdflatex
BIBTEX=bibtex

all: $(TARGET).pdf

$(TARGET).pdf : $(TARGET).bbl $(TARGET).tex
	$(LATEX) $(TARGET).tex

$(TARGET).bbl : $(TARGET).bib
	$(LATEX) $(TARGET).tex
	$(BIBTEX) $(TARGET)
	$(LATEX) $(TARGET).tex
	
clean:
	rm -rf $(addprefix $(TARGET).,pdf aux log bbl blg) *~
	
showpdf: $(TARGET).pdf
	evince $(TARGET).pdf &
