digraph <clinit> {
"$r0 = new org.unix4j.grep.util.OS$1 {2043860474}" -> "specialinvoke $r0.<org.unix4j.grep.util.OS$1: void <init>(java.lang.String,int)>(\"Windows\", 0) {824548027}";
"specialinvoke $r0.<org.unix4j.grep.util.OS$1: void <init>(java.lang.String,int)>(\"Windows\", 0) {824548027}" -> "<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Windows> = $r0 {513626957}";
"<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Windows> = $r0 {513626957}" -> "$r1 = new org.unix4j.grep.util.OS$2 {1278760896}";
"$r1 = new org.unix4j.grep.util.OS$2 {1278760896}" -> "specialinvoke $r1.<org.unix4j.grep.util.OS$2: void <init>(java.lang.String,int)>(\"Mac\", 1) {222064087}";
"specialinvoke $r1.<org.unix4j.grep.util.OS$2: void <init>(java.lang.String,int)>(\"Mac\", 1) {222064087}" -> "<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Mac> = $r1 {908414949}";
"<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Mac> = $r1 {908414949}" -> "$r2 = new org.unix4j.grep.util.OS$3 {2111612375}";
"$r2 = new org.unix4j.grep.util.OS$3 {2111612375}" -> "specialinvoke $r2.<org.unix4j.grep.util.OS$3: void <init>(java.lang.String,int)>(\"Unix\", 2) {834645729}";
"specialinvoke $r2.<org.unix4j.grep.util.OS$3: void <init>(java.lang.String,int)>(\"Unix\", 2) {834645729}" -> "<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Unix> = $r2 {363777747}";
"<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Unix> = $r2 {363777747}" -> "$r3 = new org.unix4j.grep.util.OS$4 {996852144}";
"$r3 = new org.unix4j.grep.util.OS$4 {996852144}" -> "specialinvoke $r3.<org.unix4j.grep.util.OS$4: void <init>(java.lang.String,int)>(\"Solaris\", 3) {2049516231}";
"specialinvoke $r3.<org.unix4j.grep.util.OS$4: void <init>(java.lang.String,int)>(\"Solaris\", 3) {2049516231}" -> "<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Solaris> = $r3 {622211693}";
"<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Solaris> = $r3 {622211693}" -> "$r4 = newarray (org.unix4j.grep.util.OS)[4] {437432136}";
"$r4 = newarray (org.unix4j.grep.util.OS)[4] {437432136}" -> "$r5 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Windows> {1764163481}";
"$r5 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Windows> {1764163481}" -> "$r4[0] = $r5 {1715561754}";
"$r4[0] = $r5 {1715561754}" -> "$r6 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Mac> {572438520}";
"$r6 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Mac> {572438520}" -> "$r4[1] = $r6 {1335521044}";
"$r4[1] = $r6 {1335521044}" -> "$r7 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Unix> {1772378399}";
"$r7 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Unix> {1772378399}" -> "$r4[2] = $r7 {1968859632}";
"$r4[2] = $r7 {1968859632}" -> "$r8 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Solaris> {1187430224}";
"$r8 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Solaris> {1187430224}" -> "$r4[3] = $r8 {1626692497}";
"$r4[3] = $r8 {1626692497}" -> "<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS[] $VALUES> = $r4 {514429507}";
"<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS[] $VALUES> = $r4 {514429507}" -> "return {408164791}";
}

digraph current {
"l0 = staticinvoke <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS[] values()>() {1858548116}" -> "l1 = lengthof l0 {593591570}";
"l1 = lengthof l0 {593591570}" -> "l2#5 = 0 {2039313210}";
"l2#5 = 0 {2039313210}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {886240969}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {886240969}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {1441025951}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {1441025951}" -> "if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {558093882}";
"if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {558093882}" -> "os = l0[l2#5] {208161743}";
"if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {558093882}" -> "$r0 = new java.lang.IllegalStateException {748558066}";
"os = l0[l2#5] {208161743}" -> "$z0 = virtualinvoke os.<org.unix4j.grep.util.OS: boolean isCurrent()>() {241016138}";
"$z0 = virtualinvoke os.<org.unix4j.grep.util.OS: boolean isCurrent()>() {241016138}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {1325343795}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {1325343795}" -> "if $z0 == 0 goto l2#5 = l2#5 + 1 {1135387638}";
"if $z0 == 0 goto l2#5 = l2#5 + 1 {1135387638}" -> "return os {1079058785}";
"if $z0 == 0 goto l2#5 = l2#5 + 1 {1135387638}" -> "l2#5 = l2#5 + 1 {570704876}";
"l2#5 = l2#5 + 1 {570704876}" -> "goto [?= staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5)] {76670793}";
"goto [?= staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5)] {76670793}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {886240969}";
"$r0 = new java.lang.IllegalStateException {748558066}" -> "$r1 = new java.lang.StringBuilder {1857440771}";
"$r1 = new java.lang.StringBuilder {1857440771}" -> "specialinvoke $r1.<java.lang.StringBuilder: void <init>()>() {1797395419}";
"specialinvoke $r1.<java.lang.StringBuilder: void <init>()>() {1797395419}" -> "$r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>(\"Cannot evaluate OS constant for current operating system: \") {447994442}";
"$r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>(\"Cannot evaluate OS constant for current operating system: \") {447994442}" -> "$r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {1407222124}";
"$r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {1407222124}" -> "$r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3) {551290932}";
"$r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3) {551290932}" -> "$r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>() {607774703}";
"$r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>() {607774703}" -> "specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5) {856878243}";
"specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5) {856878243}" -> "throw $r0 {1689182284}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {886240969}"[fontcolor="blue", penwidth=3, color="blue"];
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {1441025951}"[fontcolor="blue", penwidth=3, color="blue"];
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {1325343795}"[fontcolor="blue", penwidth=3, color="blue"];
}

digraph values {
"$r0 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS[] $VALUES> {1451337020}" -> "$r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>() {407535767}";
"$r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>() {407535767}" -> "$r2 = (org.unix4j.grep.util.OS[]) $r1 {475226701}";
"$r2 = (org.unix4j.grep.util.OS[]) $r1 {475226701}" -> "return $r2 {1237958185}";
}

digraph <init> {
"this := @this: org.unix4j.grep.util.OS {660027520}" -> "x0 := @parameter0: java.lang.String {882143425}";
"x0 := @parameter0: java.lang.String {882143425}" -> "x1 := @parameter1: int {123517911}";
"x1 := @parameter1: int {123517911}" -> "x2 := @parameter2: org.unix4j.grep.util.OS$1 {337964744}";
"x2 := @parameter2: org.unix4j.grep.util.OS$1 {337964744}" -> "specialinvoke this.<org.unix4j.grep.util.OS: void <init>(java.lang.String,int)>(x0, x1) {1348843232}";
"specialinvoke this.<org.unix4j.grep.util.OS: void <init>(java.lang.String,int)>(x0, x1) {1348843232}" -> "return {19552254}";
}

digraph <init> {
"this := @this: org.unix4j.grep.util.OS {1060367808}" -> "l1 := @parameter0: java.lang.String {788630784}";
"l1 := @parameter0: java.lang.String {788630784}" -> "l2 := @parameter1: int {2032266477}";
"l2 := @parameter1: int {2032266477}" -> "specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2) {1271406656}";
"specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2) {1271406656}" -> "return {996682827}";
}

digraph isCurrent {
"this := @this: org.unix4j.grep.util.OS {2036692396}" -> "$r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {417333511}";
"$r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {417333511}" -> "$r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>() {1379196540}";
"$r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>() {1379196540}" -> "$z0 = virtualinvoke this.<org.unix4j.grep.util.OS: boolean isCurrent(java.lang.String)>($r1) {1049905295}";
"$z0 = virtualinvoke this.<org.unix4j.grep.util.OS: boolean isCurrent(java.lang.String)>($r1) {1049905295}" -> "return $z0 {1458867387}";
}


