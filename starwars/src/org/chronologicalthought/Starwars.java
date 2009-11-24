package org.chronologicalthought;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;

public class Starwars {
	public void play() throws IOException, InterruptedException {
		play(67);
	}
	
	public void play(int frameLength) throws IOException, InterruptedException {
		URL res = Starwars.class.getResource("/starwars.txt");
		if ( res == null ) throw new IllegalStateException("Missing resource");
		InputStream in = res.openStream();
		try {
			InputStreamReader reader = new InputStreamReader( new BufferedInputStream(in) );
			render(reader, System.out, frameLength);
		}
		finally {
			in.close();
		}
	}
	
	private static final char ANSI_ESC = 27;
	private static final String CLEAR_SCREEN = ANSI_ESC + "[2J";

	private void render(Reader reader, PrintStream out, int frameLength) throws IOException, InterruptedException {
		boolean slash = false;
		int line = 0;
		int runlen = 1;
		StringBuilder buf = new StringBuilder();
		for (;;) {
			int r = reader.read();
			if ( r == -1 ) break;
			switch ( r ) {
			case '\\':
				if (slash) { 
					buf.append('\\'); 
					slash = false; 
				}
				else {
					slash = true;
				}
				break;
			case 'n':
				if ( slash ) {
					switch(line++) {
					case 0:
						try {
							runlen = Integer.parseInt(buf.toString());
						}
						catch (NumberFormatException e) {
							// # used as final marker
							return;
						}
						buf.setLength(0);
						break;
					case 13:
						out.print(CLEAR_SCREEN);
						out.println(buf.toString());
						buf.setLength(0);
						Thread.sleep(runlen * frameLength);
						line = 0;
						break;
					default:
						buf.append('\n');
						break;
					}
					slash = false; 
				}
				else {
					buf.append('n');
				}
				break;
			default:
				slash = false;
				buf.append((char) r);
				break;
			}
			
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		switch( args.length ) {
		case 1:
			new Starwars().play(Integer.parseInt(args[0]));
			break;
		case 0:
			new Starwars().play();
			break;
		default:
			throw new IllegalArgumentException("Unexpected arguments");
		}
	}
}
