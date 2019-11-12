package testsmell;

import com.github.javaparser.ast.CompilationUnit;

import testsmelldetectorutils.Output;
import testsmelldetectorutils.OutputList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSmell {
    public abstract String getSmellName();

    public abstract boolean getHasSmell();
    
    public abstract ArrayList<OutputList>  getOutputList();
    public abstract void  setOutputList(ArrayList<OutputList> out, String fn, String path, FileWriter fw);

    public abstract void runAnalysis(CompilationUnit testFileCompilationUnit, ArrayList<File> prodFiles) throws FileNotFoundException;

    public abstract List<SmellyElement> getSmellyElements();
}
