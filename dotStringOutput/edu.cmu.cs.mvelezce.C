digraph <init> {
"this := @this: edu.cmu.cs.mvelezce.C {1315081042}" -> "specialinvoke this.<java.lang.Object: void <init>()>() {368727462}";
"specialinvoke this.<java.lang.Object: void <init>()>() {368727462}" -> "return {1326998608}";
}

digraph recieve {
"this := @this: edu.cmu.cs.mvelezce.C {1941598182}" -> "a := @parameter0: edu.cmu.cs.mvelezce.A {379115456}";
"a := @parameter0: edu.cmu.cs.mvelezce.A {379115456}" -> "$r0 = a.<edu.cmu.cs.mvelezce.A: java.lang.Object object> {685219002}";
"$r0 = a.<edu.cmu.cs.mvelezce.A: java.lang.Object object> {685219002}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($r0) {2015455415}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($r0) {2015455415}" -> "return {703644914}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($r0) {2015455415}"[fontcolor="blue", penwidth=3, color="blue"];
}


