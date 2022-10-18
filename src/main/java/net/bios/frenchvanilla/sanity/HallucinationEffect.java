package net.bios.frenchvanilla.sanity;

public interface HallucinationEffect {
    Hallucination hallucination();
    
    // returns false when hallucination is finished
    boolean run();

    // stops the hallucination
    void stop();
}
