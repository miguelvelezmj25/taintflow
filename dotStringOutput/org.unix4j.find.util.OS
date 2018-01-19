digraph <clinit> {
"$r0 = new org.unix4j.find.util.OS$1 {834248827}" -> "specialinvoke $r0.<org.unix4j.find.util.OS$1: void <init>(java.lang.String,int)>(\"Windows\", 0) {1490231574}";
"specialinvoke $r0.<org.unix4j.find.util.OS$1: void <init>(java.lang.String,int)>(\"Windows\", 0) {1490231574}" -> "<org.unix4j.find.util.OS: org.unix4j.find.util.OS Windows> = $r0 {1686305937}";
"<org.unix4j.find.util.OS: org.unix4j.find.util.OS Windows> = $r0 {1686305937}" -> "$r1 = new org.unix4j.find.util.OS$2 {1591063164}";
"$r1 = new org.unix4j.find.util.OS$2 {1591063164}" -> "specialinvoke $r1.<org.unix4j.find.util.OS$2: void <init>(java.lang.String,int)>(\"Mac\", 1) {1717283989}";
"specialinvoke $r1.<org.unix4j.find.util.OS$2: void <init>(java.lang.String,int)>(\"Mac\", 1) {1717283989}" -> "<org.unix4j.find.util.OS: org.unix4j.find.util.OS Mac> = $r1 {1312870747}";
"<org.unix4j.find.util.OS: org.unix4j.find.util.OS Mac> = $r1 {1312870747}" -> "$r2 = new org.unix4j.find.util.OS$3 {1965151979}";
"$r2 = new org.unix4j.find.util.OS$3 {1965151979}" -> "specialinvoke $r2.<org.unix4j.find.util.OS$3: void <init>(java.lang.String,int)>(\"Unix\", 2) {496244570}";
"specialinvoke $r2.<org.unix4j.find.util.OS$3: void <init>(java.lang.String,int)>(\"Unix\", 2) {496244570}" -> "<org.unix4j.find.util.OS: org.unix4j.find.util.OS Unix> = $r2 {2011397471}";
"<org.unix4j.find.util.OS: org.unix4j.find.util.OS Unix> = $r2 {2011397471}" -> "$r3 = new org.unix4j.find.util.OS$4 {121867891}";
"$r3 = new org.unix4j.find.util.OS$4 {121867891}" -> "specialinvoke $r3.<org.unix4j.find.util.OS$4: void <init>(java.lang.String,int)>(\"Solaris\", 3) {1666621687}";
"specialinvoke $r3.<org.unix4j.find.util.OS$4: void <init>(java.lang.String,int)>(\"Solaris\", 3) {1666621687}" -> "<org.unix4j.find.util.OS: org.unix4j.find.util.OS Solaris> = $r3 {368101198}";
"<org.unix4j.find.util.OS: org.unix4j.find.util.OS Solaris> = $r3 {368101198}" -> "$r4 = newarray (org.unix4j.find.util.OS)[4] {1428622431}";
"$r4 = newarray (org.unix4j.find.util.OS)[4] {1428622431}" -> "$r5 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Windows> {1133304537}";
"$r5 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Windows> {1133304537}" -> "$r4[0] = $r5 {774140675}";
"$r4[0] = $r5 {774140675}" -> "$r6 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Mac> {1396086178}";
"$r6 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Mac> {1396086178}" -> "$r4[1] = $r6 {1585928364}";
"$r4[1] = $r6 {1585928364}" -> "$r7 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Unix> {1075976232}";
"$r7 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Unix> {1075976232}" -> "$r4[2] = $r7 {612657848}";
"$r4[2] = $r7 {612657848}" -> "$r8 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Solaris> {740507394}";
"$r8 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Solaris> {740507394}" -> "$r4[3] = $r8 {729108884}";
"$r4[3] = $r8 {729108884}" -> "<org.unix4j.find.util.OS: org.unix4j.find.util.OS[] $VALUES> = $r4 {1997126128}";
"<org.unix4j.find.util.OS: org.unix4j.find.util.OS[] $VALUES> = $r4 {1997126128}" -> "return {255746922}";
}

digraph current {
"l0 = staticinvoke <org.unix4j.find.util.OS: org.unix4j.find.util.OS[] values()>() {1933615113}" -> "l1 = lengthof l0 {587765093}";
"l1 = lengthof l0 {587765093}" -> "l2#5 = 0 {1722470396}";
"l2#5 = 0 {1722470396}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {503798979}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {503798979}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {1313792767}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {1313792767}" -> "if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {1598708643}";
"if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {1598708643}" -> "os = l0[l2#5] {135649827}";
"if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {1598708643}" -> "$r0 = new java.lang.IllegalStateException {809105363}";
"os = l0[l2#5] {135649827}" -> "$z0 = virtualinvoke os.<org.unix4j.find.util.OS: boolean isCurrent()>() {1412690628}";
"$z0 = virtualinvoke os.<org.unix4j.find.util.OS: boolean isCurrent()>() {1412690628}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {149910066}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {149910066}" -> "if $z0 == 0 goto l2#5 = l2#5 + 1 {627929814}";
"if $z0 == 0 goto l2#5 = l2#5 + 1 {627929814}" -> "return os {1301385871}";
"if $z0 == 0 goto l2#5 = l2#5 + 1 {627929814}" -> "l2#5 = l2#5 + 1 {117435435}";
"l2#5 = l2#5 + 1 {117435435}" -> "goto [?= staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5)] {1212859872}";
"goto [?= staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5)] {1212859872}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {503798979}";
"$r0 = new java.lang.IllegalStateException {809105363}" -> "$r1 = new java.lang.StringBuilder {383884087}";
"$r1 = new java.lang.StringBuilder {383884087}" -> "specialinvoke $r1.<java.lang.StringBuilder: void <init>()>() {698025185}";
"specialinvoke $r1.<java.lang.StringBuilder: void <init>()>() {698025185}" -> "$r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>(\"Cannot evaluate OS constant for current operating system: \") {294176303}";
"$r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>(\"Cannot evaluate OS constant for current operating system: \") {294176303}" -> "$r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {1474859166}";
"$r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {1474859166}" -> "$r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3) {746344351}";
"$r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3) {746344351}" -> "$r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>() {2064797057}";
"$r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>() {2064797057}" -> "specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5) {1948110536}";
"specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5) {1948110536}" -> "throw $r0 {1734532277}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {149910066}"[fontcolor="blue", penwidth=3, color="blue"];
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {1313792767}"[fontcolor="blue", penwidth=3, color="blue"];
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {503798979}"[fontcolor="blue", penwidth=3, color="blue"];
}

digraph values {
"$r0 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS[] $VALUES> {540900829}" -> "$r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>() {802855046}";
"$r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>() {802855046}" -> "$r2 = (org.unix4j.find.util.OS[]) $r1 {913605833}";
"$r2 = (org.unix4j.find.util.OS[]) $r1 {913605833}" -> "return $r2 {919776506}";
}

digraph <init> {
"this := @this: org.unix4j.find.util.OS {1236403158}" -> "x0 := @parameter0: java.lang.String {1691216632}";
"x0 := @parameter0: java.lang.String {1691216632}" -> "x1 := @parameter1: int {159062444}";
"x1 := @parameter1: int {159062444}" -> "x2 := @parameter2: org.unix4j.find.util.OS$1 {1108001271}";
"x2 := @parameter2: org.unix4j.find.util.OS$1 {1108001271}" -> "specialinvoke this.<org.unix4j.find.util.OS: void <init>(java.lang.String,int)>(x0, x1) {446505312}";
"specialinvoke this.<org.unix4j.find.util.OS: void <init>(java.lang.String,int)>(x0, x1) {446505312}" -> "return {1159901542}";
}

digraph <init> {
"this := @this: org.unix4j.find.util.OS {1774811640}" -> "l1 := @parameter0: java.lang.String {805924050}";
"l1 := @parameter0: java.lang.String {805924050}" -> "l2 := @parameter1: int {760108476}";
"l2 := @parameter1: int {760108476}" -> "specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2) {1299861837}";
"specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2) {1299861837}" -> "return {1799707382}";
}

digraph isCurrent {
"this := @this: org.unix4j.find.util.OS {1739845508}" -> "$r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {603015088}";
"$r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {603015088}" -> "$r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>() {907687345}";
"$r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>() {907687345}" -> "$z0 = virtualinvoke this.<org.unix4j.find.util.OS: boolean isCurrent(java.lang.String)>($r1) {1756214740}";
"$z0 = virtualinvoke this.<org.unix4j.find.util.OS: boolean isCurrent(java.lang.String)>($r1) {1756214740}" -> "return $z0 {1982650438}";
}


