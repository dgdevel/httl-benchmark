package httl.test.engines;

import httl.test.Benchmark;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import dgdevel.repeat.api.Template;
import dgdevel.repeat.api.exceptions.CompilerException;
import dgdevel.repeat.api.exceptions.ParseException;
import dgdevel.repeat.api.exceptions.ResolveException;
import dgdevel.repeat.predef.resolvers.ClassLoaderResolver;
import dgdevel.repeat.predef.syntax.RepeatLang;

public class Repeat implements Benchmark {

	@Override
	public String getVersion() {
		return "0.1.3";
	}

	private static Template template = null;

	static {
		try {
			template = RepeatLang
				.getContext(
						new ClassLoaderResolver(null, "httl.test.templates", "repeat", Charset.forName("UTF-8")))
					.get("books");
		} catch (ResolveException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} catch (CompilerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void execute(int times, String name, Map<String, Object> context, Object out) throws Exception {
		if (out instanceof OutputStream) {
			StringBuilder sb = new StringBuilder((int) (template.source().length() * 1.5));
			for (int i = times; i >= 0; i --) {
				sb.setLength(0);
				template.run(context, sb);
				((OutputStream) out).write(sb.toString().getBytes());
			}
		} else if (out instanceof Writer) {
			for (int i = times; i >= 0; i --) {
				template.run(context, (Writer) out);
			}
		}
		
	}

}
