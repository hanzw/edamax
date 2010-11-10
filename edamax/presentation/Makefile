TARGET=ectalk
LATEX=pdflatex -interaction=nonstopmode
BIBTEX=bibtex

all: $(TARGET).pdf

$(TARGET).pdf: $(TARGET).tex
	$(LATEX) $(TARGET).tex
	$(LATEX) $(TARGET).tex
	
clean:
	rm -rf $(addprefix $(TARGET).,pdf aux log bbl blg toc nav snm out) *~
	
showpdf: $(TARGET).pdf
	evince $(TARGET).pdf &
