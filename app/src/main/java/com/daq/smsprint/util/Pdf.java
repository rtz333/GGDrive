package com.daq.smsprint.util;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.IOException;
import java.io.OutputStream;

public class Pdf {


    public static void generatePdf(OutputStream outputStream) throws IOException {
        PdfWriter pdfWriter = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
    }
    public static void createCallLogPdf(OutputStream outputStream) throws IOException {
        PdfWriter pdfWriter = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        Table table = new Table(4);
        table.useAllAvailableWidth();

    }

    public class TableBorderRenderer extends TableRenderer {
        public TableBorderRenderer(Table modelElement) {
            super(modelElement);
        }

        @Override
        public IRenderer getNextRenderer() {
            return new TableBorderRenderer((Table) modelElement);
        }

        @Override
        protected void drawBorders(DrawContext drawContext) {
            Rectangle rect = getOccupiedAreaBBox();
            PdfPage currentPage = drawContext.getDocument().getPage(getOccupiedArea().getPageNumber());
            PdfCanvas aboveCanvas = new PdfCanvas(currentPage.newContentStreamAfter(), currentPage.getResources(), drawContext.getDocument());

            float lineWidth = 0.5f;
            rect.applyMargins(lineWidth, lineWidth, lineWidth, lineWidth, false);

//            aboveCanvas.saveState().setLineWidth(lineWidth).setStrokeColor(new DeviceRgb(255, 255, 255)).rectangle(rect).stroke().restoreState();
            aboveCanvas.saveState().setLineWidth(lineWidth).setStrokeColor(new DeviceRgb(102, 191, 63)).roundRectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight(), 10).stroke().restoreState();

            super.drawBorders(drawContext);
        }

        @Override
        public void drawChildren(DrawContext drawContext) {
            Rectangle rect = getOccupiedAreaBBox();
            float lineWidth = 0.5f;
            rect.applyMargins(lineWidth, lineWidth, lineWidth, lineWidth, false);

            PdfCanvas canvas = drawContext.getCanvas();
            canvas.saveState();
            canvas.roundRectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight(), 10);
            canvas.clip().endPath();
            super.drawChildren(drawContext);
            canvas.restoreState();
        }
    }


}
