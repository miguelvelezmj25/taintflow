    public void <init>()
    {
        edu.cmu.cs.mvelezce.C this;

        this := @this: edu.cmu.cs.mvelezce.C;

        specialinvoke this.<java.lang.Object: void <init>()>();

        return;
    }

    public void recieve(edu.cmu.cs.mvelezce.A)
    {
        edu.cmu.cs.mvelezce.C this;
        edu.cmu.cs.mvelezce.A a;
        java.lang.Object $r0;

        this := @this: edu.cmu.cs.mvelezce.C;

        a := @parameter0: edu.cmu.cs.mvelezce.A;

        $r0 = a.<edu.cmu.cs.mvelezce.A: java.lang.Object object>;

        staticinvoke <edu.cmu.cs.mvelezce.analysis.option.Sink: void sink(java.lang.Object)>($r0);

        return;
    }


