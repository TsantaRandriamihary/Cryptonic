using MailKit.Net.Smtp;
using MimeKit;
using Microsoft.Extensions.Configuration;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using System.Collections.Generic;
using MailKit.Security;

namespace Identity_Provider.Services
{
    public class PinEmailService
    {
        private readonly IConfiguration _configuration;

        public PinEmailService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task<string> LoadTemplateAsync(string templateName)
        {
            var templatePath = Path.Combine(Directory.GetCurrentDirectory(), "Templates", templateName);

            if (!File.Exists(templatePath))
                throw new FileNotFoundException($"Template {templateName} introuvable au chemin : {templatePath}");

            using var reader = new StreamReader(templatePath, Encoding.UTF8);
            return await reader.ReadToEndAsync();
        }

        public async Task SendPinEmailAsync(string to, string subject, Dictionary<string, string> variables, string templateName)
        {
            var emailSettings = _configuration.GetSection("EmailSettings");
            var message = new MimeMessage();

            message.From.Add(new MailboxAddress("Identity Provider REST API", emailSettings["SenderEmail"]));
            message.To.Add(new MailboxAddress(null, to));
            message.Subject = subject;

            var htmlContent = await LoadTemplateAsync(templateName);

            foreach (var variable in variables)
            {
                htmlContent = htmlContent.Replace($"{{{{{variable.Key}}}}}", variable.Value);
            }

            var bodyBuilder = new BodyBuilder
            {
                HtmlBody = htmlContent,
                TextBody = "Votre client email ne supporte pas HTML, veuillez utiliser un client compatible."
            };

            message.Body = bodyBuilder.ToMessageBody();

            using var client = new SmtpClient();

            try
            {
                client.ServerCertificateValidationCallback = (s, c, h, e) => true;

                bool enableSsl = bool.Parse(emailSettings["EnableSsl"]);
                await client.ConnectAsync(emailSettings["SmtpHost"], int.Parse(emailSettings["SmtpPort"]), SecureSocketOptions.Auto);
                await client.AuthenticateAsync(emailSettings["SenderEmail"], emailSettings["SenderPassword"]);
                await client.SendAsync(message);
                await client.DisconnectAsync(true);
            }
            finally
            {
                await client.DisconnectAsync(true);
            }
        }

        // Méthode pour envoyer un email de confirmation de PIN
        public async Task SendPinConfirmationEmailAsync(string to, string username, string codePin)
        {
            var variables = new Dictionary<string, string>
            {
                { "username", username },
                { "codepin", codePin }
            };

            await SendPinEmailAsync(to, "Confirmation de votre PIN", variables, "PinConfirmation.html");
        }

        // Méthode pour envoyer un email de tentative de PIN
        public async Task SendPinTentativeEmailAsync(string to, string username, string codePin)
        {
            var variables = new Dictionary<string, string>
            {
                { "username", username },
                { "codepin", codePin }
            };

            await SendPinEmailAsync(to, "Tentative de connexion avec PIN", variables, "PinTentative.html");
        }
    }
}