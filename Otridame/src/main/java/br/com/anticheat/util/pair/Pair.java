package br.com.anticheat.util.pair;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pair<X,Y> {

    private X x;
    private Y y;

}
