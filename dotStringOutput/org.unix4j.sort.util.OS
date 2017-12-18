digraph <clinit> {
"$r0 = new org.unix4j.sort.util.OS$1 {1036182338}" -> "specialinvoke $r0.<org.unix4j.sort.util.OS$1: void <init>(java.lang.String,int)>(\"Windows\", 0) {1030579960}";
"specialinvoke $r0.<org.unix4j.sort.util.OS$1: void <init>(java.lang.String,int)>(\"Windows\", 0) {1030579960}" -> "<org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Windows> = $r0 {101596878}";
"<org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Windows> = $r0 {101596878}" -> "$r1 = new org.unix4j.sort.util.OS$2 {40796362}";
"$r1 = new org.unix4j.sort.util.OS$2 {40796362}" -> "specialinvoke $r1.<org.unix4j.sort.util.OS$2: void <init>(java.lang.String,int)>(\"Mac\", 1) {680688069}";
"specialinvoke $r1.<org.unix4j.sort.util.OS$2: void <init>(java.lang.String,int)>(\"Mac\", 1) {680688069}" -> "<org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Mac> = $r1 {726544708}";
"<org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Mac> = $r1 {726544708}" -> "$r2 = new org.unix4j.sort.util.OS$3 {422732554}";
"$r2 = new org.unix4j.sort.util.OS$3 {422732554}" -> "specialinvoke $r2.<org.unix4j.sort.util.OS$3: void <init>(java.lang.String,int)>(\"Unix\", 2) {1250936841}";
"specialinvoke $r2.<org.unix4j.sort.util.OS$3: void <init>(java.lang.String,int)>(\"Unix\", 2) {1250936841}" -> "<org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Unix> = $r2 {1543271702}";
"<org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Unix> = $r2 {1543271702}" -> "$r3 = new org.unix4j.sort.util.OS$4 {1227262432}";
"$r3 = new org.unix4j.sort.util.OS$4 {1227262432}" -> "specialinvoke $r3.<org.unix4j.sort.util.OS$4: void <init>(java.lang.String,int)>(\"Solaris\", 3) {117256401}";
"specialinvoke $r3.<org.unix4j.sort.util.OS$4: void <init>(java.lang.String,int)>(\"Solaris\", 3) {117256401}" -> "<org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Solaris> = $r3 {29155737}";
"<org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Solaris> = $r3 {29155737}" -> "$r4 = newarray (org.unix4j.sort.util.OS)[4] {1572981904}";
"$r4 = newarray (org.unix4j.sort.util.OS)[4] {1572981904}" -> "$r5 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Windows> {622340804}";
"$r5 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Windows> {622340804}" -> "$r4[0] = $r5 {1995360187}";
"$r4[0] = $r5 {1995360187}" -> "$r6 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Mac> {1086284447}";
"$r6 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Mac> {1086284447}" -> "$r4[1] = $r6 {1080862252}";
"$r4[1] = $r6 {1080862252}" -> "$r7 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Unix> {51761069}";
"$r7 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Unix> {51761069}" -> "$r4[2] = $r7 {644699173}";
"$r4[2] = $r7 {644699173}" -> "$r8 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Solaris> {2007270062}";
"$r8 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Solaris> {2007270062}" -> "$r4[3] = $r8 {992226307}";
"$r4[3] = $r8 {992226307}" -> "<org.unix4j.sort.util.OS: org.unix4j.sort.util.OS[] $VALUES> = $r4 {55589239}";
"<org.unix4j.sort.util.OS: org.unix4j.sort.util.OS[] $VALUES> = $r4 {55589239}" -> "return {1398176243}";
}

digraph current {
"l0 = staticinvoke <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS[] values()>() {1541877380}" -> "l1 = lengthof l0 {1382693228}";
"l1 = lengthof l0 {1382693228}" -> "l2#5 = 0 {1009182079}";
"l2#5 = 0 {1009182079}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {1226246457}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {1226246457}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {495683207}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {495683207}" -> "if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {439388735}";
"if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {439388735}" -> "os = l0[l2#5] {1011967367}";
"if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {439388735}" -> "$r0 = new java.lang.IllegalStateException {1207362108}";
"os = l0[l2#5] {1011967367}" -> "$z0 = virtualinvoke os.<org.unix4j.sort.util.OS: boolean isCurrent()>() {484671315}";
"$z0 = virtualinvoke os.<org.unix4j.sort.util.OS: boolean isCurrent()>() {484671315}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {697194230}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {697194230}" -> "if $z0 == 0 goto l2#5 = l2#5 + 1 {436702160}";
"if $z0 == 0 goto l2#5 = l2#5 + 1 {436702160}" -> "return os {651704821}";
"if $z0 == 0 goto l2#5 = l2#5 + 1 {436702160}" -> "l2#5 = l2#5 + 1 {566674330}";
"l2#5 = l2#5 + 1 {566674330}" -> "goto [?= staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5)] {129602937}";
"goto [?= staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5)] {129602937}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {1226246457}";
"$r0 = new java.lang.IllegalStateException {1207362108}" -> "$r1 = new java.lang.StringBuilder {2081618162}";
"$r1 = new java.lang.StringBuilder {2081618162}" -> "specialinvoke $r1.<java.lang.StringBuilder: void <init>()>() {1969038396}";
"specialinvoke $r1.<java.lang.StringBuilder: void <init>()>() {1969038396}" -> "$r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>(\"Cannot evaluate OS constant for current operating system: \") {986739181}";
"$r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>(\"Cannot evaluate OS constant for current operating system: \") {986739181}" -> "$r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {337004393}";
"$r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {337004393}" -> "$r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3) {374305033}";
"$r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3) {374305033}" -> "$r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>() {799614260}";
"$r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>() {799614260}" -> "specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5) {278176856}";
"specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5) {278176856}" -> "throw $r0 {908039212}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {495683207}"[fontcolor="blue", penwidth=3, color="blue"];
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {1226246457}"[fontcolor="blue", penwidth=3, color="blue"];
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {697194230}"[fontcolor="blue", penwidth=3, color="blue"];
}

digraph values {
"$r0 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS[] $VALUES> {779690757}" -> "$r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>() {443746813}";
"$r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>() {443746813}" -> "$r2 = (org.unix4j.sort.util.OS[]) $r1 {1418420883}";
"$r2 = (org.unix4j.sort.util.OS[]) $r1 {1418420883}" -> "return $r2 {343514805}";
}

digraph <init> {
"this := @this: org.unix4j.sort.util.OS {1259498020}" -> "x0 := @parameter0: java.lang.String {444312870}";
"x0 := @parameter0: java.lang.String {444312870}" -> "x1 := @parameter1: int {471126337}";
"x1 := @parameter1: int {471126337}" -> "x2 := @parameter2: org.unix4j.sort.util.OS$1 {1247001059}";
"x2 := @parameter2: org.unix4j.sort.util.OS$1 {1247001059}" -> "specialinvoke this.<org.unix4j.sort.util.OS: void <init>(java.lang.String,int)>(x0, x1) {1543433591}";
"specialinvoke this.<org.unix4j.sort.util.OS: void <init>(java.lang.String,int)>(x0, x1) {1543433591}" -> "return {933270305}";
}

digraph <init> {
"this := @this: org.unix4j.sort.util.OS {1547850106}" -> "l1 := @parameter0: java.lang.String {4791835}";
"l1 := @parameter0: java.lang.String {4791835}" -> "l2 := @parameter1: int {1825544124}";
"l2 := @parameter1: int {1825544124}" -> "specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2) {2016217593}";
"specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2) {2016217593}" -> "return {953927846}";
}

digraph isCurrent {
"this := @this: org.unix4j.sort.util.OS {220227473}" -> "$r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {528534673}";
"$r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {528534673}" -> "$r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>() {1110725101}";
"$r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>() {1110725101}" -> "$z0 = virtualinvoke this.<org.unix4j.sort.util.OS: boolean isCurrent(java.lang.String)>($r1) {1370534762}";
"$z0 = virtualinvoke this.<org.unix4j.sort.util.OS: boolean isCurrent(java.lang.String)>($r1) {1370534762}" -> "return $z0 {1531403883}";
}


