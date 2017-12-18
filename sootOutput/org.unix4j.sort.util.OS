    static void <clinit>()
    {
        org.unix4j.sort.util.OS$1 $r0;
        org.unix4j.sort.util.OS$2 $r1;
        org.unix4j.sort.util.OS$3 $r2;
        org.unix4j.sort.util.OS$4 $r3;
        org.unix4j.sort.util.OS[] $r4;
        org.unix4j.sort.util.OS $r5, $r6, $r7, $r8;

        $r0 = new org.unix4j.sort.util.OS$1;

        specialinvoke $r0.<org.unix4j.sort.util.OS$1: void <init>(java.lang.String,int)>("Windows", 0);

        <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Windows> = $r0;

        $r1 = new org.unix4j.sort.util.OS$2;

        specialinvoke $r1.<org.unix4j.sort.util.OS$2: void <init>(java.lang.String,int)>("Mac", 1);

        <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Mac> = $r1;

        $r2 = new org.unix4j.sort.util.OS$3;

        specialinvoke $r2.<org.unix4j.sort.util.OS$3: void <init>(java.lang.String,int)>("Unix", 2);

        <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Unix> = $r2;

        $r3 = new org.unix4j.sort.util.OS$4;

        specialinvoke $r3.<org.unix4j.sort.util.OS$4: void <init>(java.lang.String,int)>("Solaris", 3);

        <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Solaris> = $r3;

        $r4 = newarray (org.unix4j.sort.util.OS)[4];

        $r5 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Windows>;

        $r4[0] = $r5;

        $r6 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Mac>;

        $r4[1] = $r6;

        $r7 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Unix>;

        $r4[2] = $r7;

        $r8 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS Solaris>;

        $r4[3] = $r8;

        <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS[] $VALUES> = $r4;

        return;
    }

    public static org.unix4j.sort.util.OS current()
    {
        org.unix4j.sort.util.OS[] l0;
        int l1, l2#5;
        org.unix4j.sort.util.OS os;
        boolean $z0;
        java.lang.IllegalStateException $r0;
        java.lang.StringBuilder $r1, $r2, $r4;
        java.lang.String $r3, $r5;

        l0 = staticinvoke <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS[] values()>();

        l1 = lengthof l0;

        l2#5 = 0;

     label1:
        staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l2#5);

        staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>(l1);

        if l2#5 >= l1 goto label3;

        os = l0[l2#5];

        $z0 = virtualinvoke os.<org.unix4j.sort.util.OS: boolean isCurrent()>();

        staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($z0);

        if $z0 == 0 goto label2;

        return os;

     label2:
        l2#5 = l2#5 + 1;

        goto label1;

     label3:
        $r0 = new java.lang.IllegalStateException;

        $r1 = new java.lang.StringBuilder;

        specialinvoke $r1.<java.lang.StringBuilder: void <init>()>();

        $r2 = virtualinvoke $r1.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>("Cannot evaluate OS constant for current operating system: ");

        $r3 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>("os.name");

        $r4 = virtualinvoke $r2.<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>($r3);

        $r5 = virtualinvoke $r4.<java.lang.StringBuilder: java.lang.String toString()>();

        specialinvoke $r0.<java.lang.IllegalStateException: void <init>(java.lang.String)>($r5);

        throw $r0;
    }

    public static org.unix4j.sort.util.OS[] values()
    {
        org.unix4j.sort.util.OS[] $r0, $r2;
        java.lang.Object $r1;

        $r0 = <org.unix4j.sort.util.OS: org.unix4j.sort.util.OS[] $VALUES>;

        $r1 = virtualinvoke $r0.<java.lang.Object: java.lang.Object clone()>();

        $r2 = (org.unix4j.sort.util.OS[]) $r1;

        return $r2;
    }

    void <init>(java.lang.String, int, org.unix4j.sort.util.OS$1)
    {
        org.unix4j.sort.util.OS this;
        java.lang.String x0;
        int x1;
        org.unix4j.sort.util.OS$1 x2;

        this := @this: org.unix4j.sort.util.OS;

        x0 := @parameter0: java.lang.String;

        x1 := @parameter1: int;

        x2 := @parameter2: org.unix4j.sort.util.OS$1;

        specialinvoke this.<org.unix4j.sort.util.OS: void <init>(java.lang.String,int)>(x0, x1);

        return;
    }

    private void <init>(java.lang.String, int)
    {
        org.unix4j.sort.util.OS this;
        java.lang.String l1;
        int l2;

        this := @this: org.unix4j.sort.util.OS;

        l1 := @parameter0: java.lang.String;

        l2 := @parameter1: int;

        specialinvoke this.<java.lang.Enum: void <init>(java.lang.String,int)>(l1, l2);

        return;
    }

    public boolean isCurrent()
    {
        org.unix4j.sort.util.OS this;
        java.lang.String $r0, $r1;
        boolean $z0;

        this := @this: org.unix4j.sort.util.OS;

        $r0 = staticinvoke <java.lang.System: java.lang.String getProperty(java.lang.String)>("os.name");

        $r1 = virtualinvoke $r0.<java.lang.String: java.lang.String toLowerCase()>();

        $z0 = virtualinvoke this.<org.unix4j.sort.util.OS: boolean isCurrent(java.lang.String)>($r1);

        return $z0;
    }


