digraph <clinit> {
"$r0 = new org.unix4j.find.util.OS$1 {888372948}" -> "specialinvoke $r0.<org.unix4j.find.util.OS$1: void <init>(java.lang.String,int)>(\"Windows\", 0) {245245365}";
"specialinvoke $r0.<org.unix4j.find.util.OS$1: void <init>(java.lang.String,int)>(\"Windows\", 0) {245245365}" -> "<org.unix4j.find.util.OS: org.unix4j.find.util.OS Windows> = $r0 {1078772074}";
"<org.unix4j.find.util.OS: org.unix4j.find.util.OS Windows> = $r0 {1078772074}" -> "$r1 = new org.unix4j.find.util.OS$2 {140020322}";
"$r1 = new org.unix4j.find.util.OS$2 {140020322}" -> "specialinvoke $r1.<org.unix4j.find.util.OS$2: void <init>(java.lang.String,int)>(\"Mac\", 1) {1120368666}";
"specialinvoke $r1.<org.unix4j.find.util.OS$2: void <init>(java.lang.String,int)>(\"Mac\", 1) {1120368666}" -> "<org.unix4j.find.util.OS: org.unix4j.find.util.OS Mac> = $r1 {2100132485}";
"<org.unix4j.find.util.OS: org.unix4j.find.util.OS Mac> = $r1 {2100132485}" -> "$r2 = new org.unix4j.find.util.OS$3 {1555286502}";
"$r2 = new org.unix4j.find.util.OS$3 {1555286502}" -> "specialinvoke $r2.<org.unix4j.find.util.OS$3: void <init>(java.lang.String,int)>(\"Unix\", 2) {2018833058}";
"specialinvoke $r2.<org.unix4j.find.util.OS$3: void <init>(java.lang.String,int)>(\"Unix\", 2) {2018833058}" -> "<org.unix4j.find.util.OS: org.unix4j.find.util.OS Unix> = $r2 {1928880169}";
"<org.unix4j.find.util.OS: org.unix4j.find.util.OS Unix> = $r2 {1928880169}" -> "$r3 = new org.unix4j.find.util.OS$4 {1947138087}";
"$r3 = new org.unix4j.find.util.OS$4 {1947138087}" -> "specialinvoke $r3.<org.unix4j.find.util.OS$4: void <init>(java.lang.String,int)>(\"Solaris\", 3) {1955857288}";
"specialinvoke $r3.<org.unix4j.find.util.OS$4: void <init>(java.lang.String,int)>(\"Solaris\", 3) {1955857288}" -> "<org.unix4j.find.util.OS: org.unix4j.find.util.OS Solaris> = $r3 {439193975}";
"<org.unix4j.find.util.OS: org.unix4j.find.util.OS Solaris> = $r3 {439193975}" -> "$r4 = newarray (org.unix4j.find.util.OS)[4] {1843187428}";
"$r4 = newarray (org.unix4j.find.util.OS)[4] {1843187428}" -> "$r5 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Windows> {684484877}";
"$r5 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Windows> {684484877}" -> "$r4[0] = $r5 {602679582}";
"$r4[0] = $r5 {602679582}" -> "$r6 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Mac> {440588810}";
"$r6 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Mac> {440588810}" -> "$r4[1] = $r6 {1478800517}";
"$r4[1] = $r6 {1478800517}" -> "$r7 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Unix> {2066406366}";
"$r7 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Unix> {2066406366}" -> "$r4[2] = $r7 {114804898}";
"$r4[2] = $r7 {114804898}" -> "$r8 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Solaris> {1792190683}";
"$r8 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS Solaris> {1792190683}" -> "$r4[3] = $r8 {1392046075}";
"$r4[3] = $r8 {1392046075}" -> "<org.unix4j.find.util.OS: org.unix4j.find.util.OS[] $VALUES> = $r4 {1505612407}";
"<org.unix4j.find.util.OS: org.unix4j.find.util.OS[] $VALUES> = $r4 {1505612407}" -> "return {1163148168}";
}

digraph current {
"l0 = staticinvoke <org.unix4j.find.util.OS: org.unix4j.find.util.OS[] values()>() {349458034}" -> "l1 = lengthof l0 {2045856854}";
"l1 = lengthof l0 {2045856854}" -> "l2#5 = 0 {1027017168}";
"l2#5 = 0 {1027017168}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {1337306552}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {1337306552}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {620914927}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {620914927}" -> "if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {1776542169}";
"if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {1776542169}" -> "os = l0[l2#5] {1281898359}";
"if l2#5 >= l1 goto $r0 = new java.lang.IllegalStateException {1776542169}" -> "$r0 = new java.lang.IllegalStateException {1063470600}";
"os = l0[l2#5] {1281898359}" -> "$z0 = virtualinvoke os.<org.unix4j.find.util.OS: boolean isCurrent()>() {132058033}";
"$z0 = virtualinvoke os.<org.unix4j.find.util.OS: boolean isCurrent()>() {132058033}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {122473104}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {122473104}" -> "if $z0 == 0 goto l2#5 = l2#5 + 1 {938253385}";
"if $z0 == 0 goto l2#5 = l2#5 + 1 {938253385}" -> "return os {1064249035}";
"if $z0 == 0 goto l2#5 = l2#5 + 1 {938253385}" -> "l2#5 = l2#5 + 1 {1074933780}";
"l2#5 = l2#5 + 1 {1074933780}" -> "goto [?= staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5)] {332391051}";
"goto [?= staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5)] {332391051}" -> "staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {1337306552}";
"$r0 = new java.lang.IllegalStateException {1063470600}" -> "$r1 = new java.lang.StringBuilder {1149771724}";
"$r1 = new java.lang.StringBuilder {1149771724}" -> "specialinvoke $r1.<java.lang.StringBuilder: void <init>()>() {681130615}";
"specialinvoke $r1.<java.lang.StringBuilder: void <init>()>() {681130615}" -> "$r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>(\"Cannot evaluate OS constant for current operating system: \") {1058309571}";
"$r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>(\"Cannot evaluate OS constant for current operating system: \") {1058309571}" -> "$r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {1984688653}";
"$r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {1984688653}" -> "$r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3) {56868765}";
"$r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3) {56868765}" -> "$r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>() {406984879}";
"$r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>() {406984879}" -> "specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5) {242492347}";
"specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5) {242492347}" -> "throw $r0 {654730210}";
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1) {620914927}"[fontcolor="blue", penwidth=3, color="blue"];
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5) {1337306552}"[fontcolor="blue", penwidth=3, color="blue"];
"staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0) {122473104}"[fontcolor="blue", penwidth=3, color="blue"];
}

digraph values {
"$r0 = <org.unix4j.find.util.OS: org.unix4j.find.util.OS[] $VALUES> {18070049}" -> "$r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>() {1847419884}";
"$r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>() {1847419884}" -> "$r2 = (org.unix4j.find.util.OS[]) $r1 {1041998166}";
"$r2 = (org.unix4j.find.util.OS[]) $r1 {1041998166}" -> "return $r2 {1987277104}";
}

digraph <init> {
"this := @this: org.unix4j.find.util.OS {2096900616}" -> "x0 := @parameter0: java.lang.String {1722141669}";
"x0 := @parameter0: java.lang.String {1722141669}" -> "x1 := @parameter1: int {2124432268}";
"x1 := @parameter1: int {2124432268}" -> "x2 := @parameter2: org.unix4j.find.util.OS$1 {1887692104}";
"x2 := @parameter2: org.unix4j.find.util.OS$1 {1887692104}" -> "specialinvoke this.<org.unix4j.find.util.OS: void <init>(java.lang.String,int)>(x0, x1) {2078011503}";
"specialinvoke this.<org.unix4j.find.util.OS: void <init>(java.lang.String,int)>(x0, x1) {2078011503}" -> "return {471582980}";
}

digraph <init> {
"this := @this: org.unix4j.find.util.OS {884304474}" -> "l1 := @parameter0: java.lang.String {463473676}";
"l1 := @parameter0: java.lang.String {463473676}" -> "l2 := @parameter1: int {625868084}";
"l2 := @parameter1: int {625868084}" -> "specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2) {1515604577}";
"specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2) {1515604577}" -> "return {988705496}";
}

digraph isCurrent {
"this := @this: org.unix4j.find.util.OS {1000165912}" -> "$r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {965699106}";
"$r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>(\"os.name\") {965699106}" -> "$r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>() {418836109}";
"$r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>() {418836109}" -> "$z0 = virtualinvoke this.<org.unix4j.find.util.OS: boolean isCurrent(java.lang.String)>($r1) {1513195333}";
"$z0 = virtualinvoke this.<org.unix4j.find.util.OS: boolean isCurrent(java.lang.String)>($r1) {1513195333}" -> "return $z0 {759431771}";
}


