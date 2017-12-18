digraph <clinit> {
"$r0 = new org.unix4j.grep.util.OS$1 {974825781}" -> "specialinvoke $r0.<org.unix4j.grep.util.OS$1: void <init>(java.lang.String,int)>(\"Windows\", 0) {934834490}";
"specialinvoke $r0.<org.unix4j.grep.util.OS$1: void <init>(java.lang.String,int)>(\"Windows\", 0) {934834490}" -> "<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Windows> = $r0 {28444449}";
"<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Windows> = $r0 {28444449}" -> "$r1 = new org.unix4j.grep.util.OS$2 {1298769294}";
"$r1 = new org.unix4j.grep.util.OS$2 {1298769294}" -> "specialinvoke $r1.<org.unix4j.grep.util.OS$2: void <init>(java.lang.String,int)>(\"Mac\", 1) {671234089}";
"specialinvoke $r1.<org.unix4j.grep.util.OS$2: void <init>(java.lang.String,int)>(\"Mac\", 1) {671234089}" -> "<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Mac> = $r1 {547918065}";
"<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Mac> = $r1 {547918065}" -> "$r2 = new org.unix4j.grep.util.OS$3 {1893137501}";
"$r2 = new org.unix4j.grep.util.OS$3 {1893137501}" -> "specialinvoke $r2.<org.unix4j.grep.util.OS$3: void <init>(java.lang.String,int)>(\"Unix\", 2) {2142078877}";
"specialinvoke $r2.<org.unix4j.grep.util.OS$3: void <init>(java.lang.String,int)>(\"Unix\", 2) {2142078877}" -> "<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Unix> = $r2 {1665453723}";
"<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Unix> = $r2 {1665453723}" -> "$r3 = new org.unix4j.grep.util.OS$4 {323264123}";
"$r3 = new org.unix4j.grep.util.OS$4 {323264123}" -> "specialinvoke $r3.<org.unix4j.grep.util.OS$4: void <init>(java.lang.String,int)>(\"Solaris\", 3) {1067857442}";
"specialinvoke $r3.<org.unix4j.grep.util.OS$4: void <init>(java.lang.String,int)>(\"Solaris\", 3) {1067857442}" -> "<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Solaris> = $r3 {1765883006}";
"<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Solaris> = $r3 {1765883006}" -> "$r4 = newarray (org.unix4j.grep.util.OS)[4] {94485767}";
"$r4 = newarray (org.unix4j.grep.util.OS)[4] {94485767}" -> "$r5 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Windows> {420895444}";
"$r5 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Windows> {420895444}" -> "$r4[0] = $r5 {290948468}";
"$r4[0] = $r5 {290948468}" -> "$r6 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Mac> {1232290153}";
"$r6 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Mac> {1232290153}" -> "$r4[1] = $r6 {1679950102}";
"$r4[1] = $r6 {1679950102}" -> "$r7 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Unix> {1666067034}";
"$r7 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Unix> {1666067034}" -> "$r4[2] = $r7 {395357588}";
"$r4[2] = $r7 {395357588}" -> "$r8 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Solaris> {790264182}";
"$r8 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS Solaris> {790264182}" -> "$r4[3] = $r8 {1031052556}";
"$r4[3] = $r8 {1031052556}" -> "<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS[] $VALUES> = $r4 {153647080}";
"<org.unix4j.grep.util.OS: org.unix4j.grep.util.OS[] $VALUES> = $r4 {153647080}" -> "return {283543825}";
}

digraph current {
"l0 = staticinvoke <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS[] values()>() {509101689}" -> "l1 = lengthof l0 {1999510043}";
"l1 = lengthof l0 {1999510043}" -> "l2#5 = 0 {1898819868}";
"l2#5 = 0 {1898819868}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {514488219}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {514488219}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {1023566063}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {1023566063}" -> "if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {1328690903}";
"if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {1328690903}" -> "os = l0[l2#5] {252727576}";
"if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {1328690903}" -> "$r0 = new java.lang.IllegalStateException {430376790}";
"os = l0[l2#5] {252727576}" -> "$z0 = virtualinvoke os.<org.unix4j.grep.util.OS: boolean isCurrent()>() {204454366}";
"$z0 = virtualinvoke os.<org.unix4j.grep.util.OS: boolean isCurrent()>() {204454366}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {754216621}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {754216621}" -> "if $z0 == 0 goto l2#5 = l2#5 + 1 {1481382360}";
"if $z0 == 0 goto l2#5 = l2#5 + 1 {1481382360}" -> "return os {1301525222}";
"if $z0 == 0 goto l2#5 = l2#5 + 1 {1481382360}" -> "l2#5 = l2#5 + 1 {1015219139}";
"l2#5 = l2#5 + 1 {1015219139}" -> "goto [?= staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5)] {70868818}";
"goto [?= staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5)] {70868818}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {514488219}";
"$r0 = new java.lang.IllegalStateException {430376790}" -> "$r1 = new java.lang.StringBuilder {591858900}";
"$r1 = new java.lang.StringBuilder {591858900}" -> "specialinvoke $r1.<java.lang.StringBuilder: void <init>()>() {1792364962}";
"specialinvoke $r1.<java.lang.StringBuilder: void <init>()>() {1792364962}" -> "$r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>(\"Cannot evaluate OS constant for current operating system: \") {710769706}";
"$r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>(\"Cannot evaluate OS constant for current operating system: \") {710769706}" -> "$r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {1453977905}";
"$r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {1453977905}" -> "$r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3) {1506134453}";
"$r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3) {1506134453}" -> "$r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>() {406340235}";
"$r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>() {406340235}" -> "specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5) {29420226}";
"specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5) {29420226}" -> "throw $r0 {419013554}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {754216621}"[fontcolor="blue", penwidth=3, color="blue"];
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {514488219}"[fontcolor="blue", penwidth=3, color="blue"];
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {1023566063}"[fontcolor="blue", penwidth=3, color="blue"];
}

digraph values {
"$r0 = <org.unix4j.grep.util.OS: org.unix4j.grep.util.OS[] $VALUES> {1031878610}" -> "$r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>() {2055117189}";
"$r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>() {2055117189}" -> "$r2 = (org.unix4j.grep.util.OS[]) $r1 {1321429185}";
"$r2 = (org.unix4j.grep.util.OS[]) $r1 {1321429185}" -> "return $r2 {232874453}";
}

digraph <init> {
"this := @this: org.unix4j.grep.util.OS {1780817034}" -> "x0 := @parameter0: java.lang.String {1566020146}";
"x0 := @parameter0: java.lang.String {1566020146}" -> "x1 := @parameter1: int {1167625901}";
"x1 := @parameter1: int {1167625901}" -> "x2 := @parameter2: org.unix4j.grep.util.OS$1 {1952343604}";
"x2 := @parameter2: org.unix4j.grep.util.OS$1 {1952343604}" -> "specialinvoke this.<org.unix4j.grep.util.OS: void <init>(java.lang.String,int)>(x0, x1) {386475449}";
"specialinvoke this.<org.unix4j.grep.util.OS: void <init>(java.lang.String,int)>(x0, x1) {386475449}" -> "return {1447913190}";
}

digraph <init> {
"this := @this: org.unix4j.grep.util.OS {1039934933}" -> "l1 := @parameter0: java.lang.String {713292118}";
"l1 := @parameter0: java.lang.String {713292118}" -> "l2 := @parameter1: int {139526146}";
"l2 := @parameter1: int {139526146}" -> "specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2) {233079860}";
"specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2) {233079860}" -> "return {588996947}";
}

digraph isCurrent {
"this := @this: org.unix4j.grep.util.OS {309451115}" -> "$r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {188630789}";
"$r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {188630789}" -> "$r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>() {1210547390}";
"$r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>() {1210547390}" -> "$z0 = virtualinvoke this.<org.unix4j.grep.util.OS: boolean isCurrent(java.lang.String)>($r1) {19984962}";
"$z0 = virtualinvoke this.<org.unix4j.grep.util.OS: boolean isCurrent(java.lang.String)>($r1) {19984962}" -> "return $z0 {1696628269}";
}


