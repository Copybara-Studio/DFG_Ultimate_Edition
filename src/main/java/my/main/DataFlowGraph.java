/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package my.main;

import my.sfc.SequentialFunctionChart;

/**
 *
 * @author Kay Jay O'Nail
 */
public class DataFlowGraph
{
    public static void main(String[] args)
    {
        try {
//            Extractor ex = new Extractor("plc.xml");
//            ex.getTextFromFile();
//            ex.printTerms();
            SequentialFunctionChart sfc = new SequentialFunctionChart();
            sfc.readFromXML("plc.xml");
            sfc.printSFC();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
