package com.jz.pdf.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jz.pdf.R;
import com.jz.pdf.activity.PdfIntroActivity;
import com.jz.pdf.adapter.PDFRecyclerViewAdapter;
import com.jz.pdf.aws.DynamoDBManager;
import com.jz.pdf.base.App;
import com.jz.pdf.base.BaseFragment;
import com.jz.pdf.bean.PDFInfos;
import com.jz.pdf.utils.CommonUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by and2long on 16-8-29.
 */
public class PdfListFragment extends BaseFragment {

    @Bind(R.id.rv_pdfList)
    RecyclerView rvPdfList;
    private int itemHeight;
    private ArrayList<PDFInfos> pdfList;
    private Intent intent;


    @Override
    public View getSuccessView() {
        View view = View.inflate(App.appContext, R.layout.fragmet_pdflist, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public Object getLoadData() {
        pdfList = DynamoDBManager.getPdfList();
                /*for (int i = 0; i < pdfList.size(); i++) {
                    System.out.println(pdfList.get(i).toString());
                }*/
        itemHeight = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 4;
        CommonUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView.LayoutManager manager = new LinearLayoutManager(App.appContext);
                PDFRecyclerViewAdapter adapter = new PDFRecyclerViewAdapter(pdfList, itemHeight);
                adapter.setOnItemClickLitener(new PDFRecyclerViewAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        intent = new Intent(App.appContext, PdfIntroActivity.class);
                        //intent.putExtra("pdfName", pdfList.get(position).getPDFFileName());
                        intent.putExtra("pdfInfos", pdfList.get(position));
                        startActivity(intent);
                    }
                });
                rvPdfList.setLayoutManager(manager);
                rvPdfList.setAdapter(adapter);
            }
        });
        return pdfList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
